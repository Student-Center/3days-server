package com.threedays.application.auth.service

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.config.AuthTesterProperties
import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.application.auth.port.inbound.VerifyExistingUserAuthCode
import com.threedays.application.auth.port.inbound.VerifyNewUserAuthCode
import com.threedays.application.auth.port.outbound.AuthCodeSmsSenderSpy
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.repository.AuthCodeRepositorySpy
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.entity.UserProfile
import com.threedays.domain.user.entity.UserProfileImage
import com.threedays.domain.user.repository.UserRepositorySpy
import com.threedays.support.common.security.jwt.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.time.LocalDateTime

@DisplayName("[서비스][인증] - AuthCode(인증 코드)")
class AuthCodeServiceTest : DescribeSpec({

    val fixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()

    val tokenSecret = "secret"
    val expirationSeconds = 60L

    val userRepository = UserRepositorySpy()
    val authCodeRepository = AuthCodeRepositorySpy()
    val authCodeSmsSender = AuthCodeSmsSenderSpy()
    val authProperties = AuthProperties(
        tokenSecret = tokenSecret,
        authCodeExpirationSeconds = expirationSeconds,
        registerTokenExpirationSeconds = expirationSeconds,
        accessTokenExpirationSeconds = expirationSeconds,
        refreshTokenExpirationSeconds = expirationSeconds,
    )
    val authTesterProperties = fixtureMonkey.giveMeBuilder<AuthTesterProperties>().sample()
    val issueLoginTokens = IssueLoginTokensStub(authProperties)
    val authCodeService = AuthCodeService(
        userRepository = userRepository,
        authCodeRepository = authCodeRepository,
        authCodeSmsSender = authCodeSmsSender,
        issueLoginTokens = issueLoginTokens,
        authProperties = authProperties,
        authTesterProperties = authTesterProperties,
    )

    beforeEach {
        authCodeRepository.clear()
        authCodeSmsSender.clear()
        userRepository.clear()
    }

    describe("SendAuthCode : 인증 코드 발송") {
        context("해당 번호로 가입된 유저가 없는 경우") {
            it("인증코드를 생성하고, SMS를 발송한다. 새로운 유저응답을 반환한다") {
                // arrange
                val command: SendAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<SendAuthCode.Command>()
                    .set(SendAuthCode.Command::phoneNumber, PhoneNumber("01012345678"))
                    .sample()

                // act
                val result: SendAuthCode.Result = authCodeService.invoke(command)

                // assert
                authCodeRepository.find(result.authCode.id) shouldBe result.authCode
                authCodeSmsSender.find(result.authCode.id) shouldBe result.authCode
                result shouldBe SendAuthCode.Result.NewUser(result.authCode)
            }
        }

        context("해당 번호로 가입된 유저가 있는 경우") {
            it("인증코드를 생성하고, SMS를 발송한다. 기존 유저응답을 반환한다") {
                // arrange
                val phoneNumber = PhoneNumber("01012345678")
                val userProfile = fixtureMonkey
                    .giveMeBuilder<UserProfile>()
                    .set(UserProfile::company, null)
                    .sample()

                val userDesiredPartner = fixtureMonkey
                    .giveMeBuilder<UserDesiredPartner>()
                    .set(UserDesiredPartner::allowSameCompany, null)
                    .sample()


                val userProfileImageUrl: URL = URL("http://example.com")
                val userProfileImage = fixtureMonkey
                    .giveMeBuilder<UserProfileImage>()
                    .set(UserProfileImage::url, userProfileImageUrl)
                    .sampleList(1)


                fixtureMonkey
                    .giveMeBuilder<User>()
                    .set(User::name, User.Name("홍길동"))
                    .set(User::phoneNumber, phoneNumber)
                    .set(User::profile, userProfile)
                    .set(User::profileImages, userProfileImage)
                    .set(User::desiredPartner, userDesiredPartner)
                    .sample()
                    .also { userRepository.save(it) }

                val command: SendAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<SendAuthCode.Command>()
                    .set(SendAuthCode.Command::phoneNumber, phoneNumber)
                    .sample()

                // act
                val result: SendAuthCode.Result = authCodeService.invoke(command)

                // assert
                authCodeRepository.find(result.authCode.id) shouldBe result.authCode
                authCodeSmsSender.find(result.authCode.id) shouldBe result.authCode
                result shouldBe SendAuthCode.Result.ExistingUser(result.authCode)
            }
        }
    }

    describe("VerifyNewUserAuthCode : 새로운 유저 인증 코드 검증") {

        context("인증코드가 만료되어 존재하지 않으면") {
            it("인증 코드 검증 시, AuthException.AuthCodeExpiredException 예외를 발생시킨다") {
                // arrange
                val authCode: AuthCode = AuthCode.create(
                    clientOS = ClientOS.AOS,
                    phoneNumber = PhoneNumber("01012345678"),
                    expireAt = LocalDateTime.now().minusSeconds(1)
                )
                authCodeRepository.save(authCode)

                val command: VerifyNewUserAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<VerifyNewUserAuthCode.Command>()
                    .set(VerifyNewUserAuthCode.Command::code, authCode.code)
                    .set(VerifyNewUserAuthCode.Command::id, authCode.id)
                    .sample()

                // act, assert
                shouldThrow<AuthException.AuthCodeExpiredException> { authCodeService.invoke(command) }
            }
        }

        context("인증코드가 유효하지 않으면") {
            it("인증 코드 검증 시, AuthException.InvalidAuthCodeException 예외를 발생시킨다") {
                // arrange
                val invalidCodeNumber = AuthCode.Code("000000")
                val authCode: AuthCode = AuthCode.create(
                    clientOS = ClientOS.AOS,
                    phoneNumber = PhoneNumber("01012345678"),
                    expireAt = LocalDateTime.now().plusSeconds(expirationSeconds)
                )
                authCodeRepository.save(authCode)

                val command: VerifyNewUserAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<VerifyNewUserAuthCode.Command>()
                    .set(VerifyNewUserAuthCode.Command::code, invalidCodeNumber)
                    .set(VerifyNewUserAuthCode.Command::id, authCode.id)
                    .sample()

                // act, assert
                shouldThrow<AuthException.InvalidAuthCodeException> { authCodeService.invoke(command) }
            }
        }

        context("이미 해당 번호로 가입된 사용자가 있으면") {
            it("인증 코드 검증 시, AuthException.UserExistsException 예외를 발생시킨다") {
                // arrange
                val phoneNumber = PhoneNumber("01012345678")
                val userProfile = fixtureMonkey
                    .giveMeBuilder<UserProfile>()
                    .set(UserProfile::company, null)
                    .sample()

                val userDesiredPartner = fixtureMonkey
                    .giveMeBuilder<UserDesiredPartner>()
                    .set(UserDesiredPartner::allowSameCompany, null)
                    .sample()

                val userProfileImageUrl: URL = URL("http://example.com")
                val userProfileImage = fixtureMonkey
                    .giveMeBuilder<UserProfileImage>()
                    .set(UserProfileImage::url, userProfileImageUrl)
                    .sampleList(1)

                fixtureMonkey
                    .giveMeBuilder<User>()
                    .set(User::name, User.Name("홍길동"))
                    .set(User::phoneNumber, phoneNumber)
                    .set(User::profileImages, userProfileImage)
                    .set(User::profile, userProfile)
                    .set(User::desiredPartner, userDesiredPartner)
                    .sample()
                    .also { userRepository.save(it) }

                val authCode: AuthCode = AuthCode.create(
                    clientOS = ClientOS.AOS,
                    phoneNumber = phoneNumber,
                    expireAt = LocalDateTime.now().plusSeconds(expirationSeconds)
                )
                authCodeRepository.save(authCode)

                val command: VerifyNewUserAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<VerifyNewUserAuthCode.Command>()
                    .set(VerifyNewUserAuthCode.Command::code, authCode.code)
                    .set(VerifyNewUserAuthCode.Command::id, authCode.id)
                    .sample()

                // act, assert
                shouldThrow<AuthException.UserExistsException> { authCodeService.invoke(command) }
            }
        }

        context("인증코드가 유효하면") {
            it("인증 코드 검증 후, RegisterToken을 생성하여 반환한다") {
                // arrange
                val authCode: AuthCode = AuthCode.create(
                    clientOS = ClientOS.AOS,
                    phoneNumber = PhoneNumber("01012345678"),
                    expireAt = LocalDateTime.now().plusSeconds(expirationSeconds)
                )
                authCodeRepository.save(authCode)

                val command: VerifyNewUserAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<VerifyNewUserAuthCode.Command>()
                    .set(VerifyNewUserAuthCode.Command::code, authCode.code)
                    .set(VerifyNewUserAuthCode.Command::id, authCode.id)
                    .sample()

                // act
                val result: VerifyNewUserAuthCode.Result = authCodeService.invoke(command)

                // assert
                JwtTokenProvider.verifyToken(
                    result.registerToken.value,
                    tokenSecret
                ).isSuccess shouldBe true
            }
        }
    }

    describe("VerifyExistingUserAuthCode : 기존 유저 인증 코드 검증") {
        fun createAuthCode(expireAt: LocalDateTime): Pair<AuthCode, VerifyExistingUserAuthCode.Command> {
            val authCode = AuthCode.create(
                clientOS = ClientOS.AOS,
                phoneNumber = PhoneNumber("01012345678"),
                expireAt = expireAt
            )
            authCodeRepository.save(authCode)

            val command = fixtureMonkey
                .giveMeBuilder<VerifyExistingUserAuthCode.Command>()
                .set(VerifyExistingUserAuthCode.Command::code, authCode.code)
                .set(VerifyExistingUserAuthCode.Command::id, authCode.id)
                .sample()

            return Pair(authCode, command)
        }

        context("인증코드가 만료되어 존재하지 않으면") {
            it("AuthException.AuthCodeExpiredException 예외를 발생시킨다") {
                // arrange
                val (_, command) = createAuthCode(LocalDateTime.now().minusSeconds(1))

                // act, assert
                shouldThrow<AuthException.AuthCodeExpiredException> {
                    authCodeService.invoke(command)
                }
            }
        }

        context("인증코드가 유효하지 않으면") {
            it("AuthException.InvalidAuthCodeException 예외를 발생시킨다") {
                // arrange
                val (authCode, _) = createAuthCode(
                    LocalDateTime.now().plusSeconds(expirationSeconds)
                )

                val invalidCommand = fixtureMonkey
                    .giveMeBuilder<VerifyExistingUserAuthCode.Command>()
                    .set(VerifyExistingUserAuthCode.Command::code, AuthCode.Code("000000"))
                    .set(VerifyExistingUserAuthCode.Command::id, authCode.id)
                    .sample()

                // act, assert
                shouldThrow<AuthException.InvalidAuthCodeException> {
                    authCodeService.invoke(invalidCommand)
                }
            }
        }

        context("인증코드가 유효하면") {
            it("AccessToken과 RefreshToken을 생성하여 반환한다") {
                // arrange
                val phoneNumber = PhoneNumber("01012345678")
                val userProfile = fixtureMonkey
                    .giveMeBuilder<UserProfile>()
                    .set(UserProfile::company, null)
                    .sample()

                val userDesiredPartner = fixtureMonkey
                    .giveMeBuilder<UserDesiredPartner>()
                    .set(UserDesiredPartner::allowSameCompany, null)
                    .sample()


                val userProfileImageUrl: URL = URL("http://example.com")
                val userProfileImage = fixtureMonkey
                    .giveMeBuilder<UserProfileImage>()
                    .set(UserProfileImage::url, userProfileImageUrl)
                    .sampleList(1)

                fixtureMonkey
                    .giveMeBuilder<User>()
                    .set(User::name, User.Name("홍길동"))
                    .set(User::phoneNumber, phoneNumber)
                    .set(User::profile, userProfile)
                    .set(User::profileImages, userProfileImage)
                    .set(User::desiredPartner, userDesiredPartner)
                    .sample()
                    .also { userRepository.save(it) }

                val (_, command) = createAuthCode(
                    LocalDateTime.now().plusSeconds(expirationSeconds)
                )

                // act
                val result: VerifyExistingUserAuthCode.Result = authCodeService.invoke(command)

                // assert
                JwtTokenProvider.verifyToken(
                    result.accessToken.value,
                    tokenSecret
                ).isSuccess shouldBe true
                JwtTokenProvider.verifyToken(
                    result.refreshToken.value,
                    tokenSecret
                ).isSuccess shouldBe true
            }
        }
    }
})

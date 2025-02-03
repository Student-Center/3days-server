package com.threedays.application.auth.service

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.ClearTokens
import com.threedays.application.auth.port.inbound.IssueLoginTokens
import com.threedays.application.auth.port.inbound.RefreshLoginTokens
import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.repository.RefreshTokenRepositorySpy
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.entity.UserProfile
import com.threedays.domain.user.entity.UserProfileImage
import com.threedays.support.common.base.domain.UUIDTypeId
import com.threedays.support.common.security.jwt.JwtTokenProvider
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.net.URL

@DisplayName("[서비스][인증] - AuthToken(인증 토큰)")
class AuthTokenServiceTest : DescribeSpec({

    val fixtureMonkey = FixtureMonkey
        .builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()
    val secret = "secret"
    val accessTokenExpirationSeconds = 60
    val refreshTokenExpirationSeconds = 60
    val authProperties = fixtureMonkey
        .giveMeBuilder<AuthProperties>()
        .set(AuthProperties::tokenSecret, secret)
        .set(AuthProperties::accessTokenExpirationSeconds, accessTokenExpirationSeconds)
        .set(AuthProperties::refreshTokenExpirationSeconds, refreshTokenExpirationSeconds)
        .sample()
    val refreshTokenRepository = RefreshTokenRepositorySpy()

    val sut = AuthTokenService(
        authProperties = authProperties,
        refreshTokenRepository = refreshTokenRepository
    )

    afterEach {
        refreshTokenRepository.clear()
    }

    describe("로그인 토큰 발급") {
        it("로그인 토큰을 발급하고, RefreshToken을 저장한다") {
            // arrange
            val userId = UUIDTypeId.random<User.Id>()
            val userProfile = fixtureMonkey
                .giveMeBuilder<UserProfile>()
                .set(UserProfile::id, userId)
                .set(UserProfile::company, null)
                .sample()

            val userDesiredPartner = fixtureMonkey
                .giveMeBuilder<UserDesiredPartner>()
                .set(UserDesiredPartner::id, userId)
                .set(UserDesiredPartner::allowSameCompany, null)
                .sample()


            val userProfileImageUrl: URL = URL("http://example.com")
            val userProfileImage = fixtureMonkey
                .giveMeBuilder<UserProfileImage>()
                .set(UserProfileImage::url, userProfileImageUrl)
                .sampleList(1)


            val user = fixtureMonkey
                .giveMeBuilder<User>()
                .set(User::name, User.Name("홍길동"))
                .set(User::id, userId)
                .set(User::profile, userProfile)
                .set(User::profileImages, userProfileImage)
                .set(User::desiredPartner, userDesiredPartner)
                .set(User::phoneNumber, PhoneNumber("01012345678"))
                .sample()

            val command = fixtureMonkey
                .giveMeBuilder<IssueLoginTokens.Command>()
                .set(IssueLoginTokens.Command::user, user)
                .sample()

            // act
            val result: IssueLoginTokens.Result = sut.invoke(command)

            // assert
            JwtTokenProvider
                .verifyToken(result.accessToken.value, secret)
                .isSuccess shouldBe true

            JwtTokenProvider
                .verifyToken(result.refreshToken.value, secret)
                .isSuccess shouldBe true

            result.accessTokenExpiresIn shouldBe accessTokenExpirationSeconds
            result.refreshTokenExpiresIn shouldBe refreshTokenExpirationSeconds

            refreshTokenRepository.find(userId) shouldBe result.refreshToken
        }
    }


    describe("로그인 토큰 갱신") {
        it("새로운 AccessToken과 RefreshToken을 발급하고, RefreshToken을 저장한다") {
            // arrange
            val userId = UUIDTypeId.random<User.Id>()
            val refreshToken = RefreshToken.generate(
                secret = secret,
                expirationSeconds = refreshTokenExpirationSeconds.toLong(),
                userId = userId
            )

            refreshTokenRepository.save(refreshToken, refreshTokenExpirationSeconds.toLong())
            val command = fixtureMonkey
                .giveMeBuilder<RefreshLoginTokens.Command>()
                .set(RefreshLoginTokens.Command::refreshToken, refreshToken.value)
                .sample()

            // act
            val result: RefreshLoginTokens.Result = sut.invoke(command)

            // assert
            JwtTokenProvider
                .verifyToken(result.accessToken, secret)
                .isSuccess shouldBe true

            JwtTokenProvider
                .verifyToken(result.refreshToken, secret)
                .isSuccess shouldBe true

            result.expiresIn shouldBe accessTokenExpirationSeconds

            refreshTokenRepository.find(userId) shouldBe RefreshToken(
                value = result.refreshToken,
                userId = userId
            )
        }

        context("RefreshToken이 저장되어있지 않은 경우") {
            it("RefreshTokenExpiredException을 던진다") {
                // arrange
                val refreshToken = RefreshToken.generate(
                    secret = secret,
                    expirationSeconds = refreshTokenExpirationSeconds.toLong(),
                    userId = UUIDTypeId.random<User.Id>()
                )

                val command = fixtureMonkey
                    .giveMeBuilder<RefreshLoginTokens.Command>()
                    .set(RefreshLoginTokens.Command::refreshToken, refreshToken.value)
                    .sample()

                // act & assert
                shouldThrow<AuthException.RefreshTokenExpiredException> {
                    sut.invoke(command)
                }
            }
        }

        context("RefreshToken이 일치하지 않는 경우") {
            it("InvalidRefreshTokenException을 던진다") {
                // arrange
                val userId = UUIDTypeId.random<User.Id>()
                val refreshToken = RefreshToken.generate(
                    secret = secret,
                    expirationSeconds = refreshTokenExpirationSeconds.toLong(),
                    userId = userId
                )

                refreshTokenRepository.save(refreshToken, refreshTokenExpirationSeconds.toLong())
                val command = fixtureMonkey
                    .giveMeBuilder<RefreshLoginTokens.Command>()
                    .set(RefreshLoginTokens.Command::refreshToken, refreshToken.value + "invalid")
                    .sample()

                // act & assert
                shouldThrow<AuthException.InvalidRefreshTokenException> {
                    sut.invoke(command)
                }
            }
        }
    }

    describe("리프레시 토큰 삭제") {

        context("RefreshToken이 저장되어있지 않은 경우") {
            it("에러를 반환하지 않는다.") {
                // arrange
                val userId = UUIDTypeId.random<User.Id>()
                val command = fixtureMonkey
                    .giveMeBuilder<ClearTokens.Command>()
                    .set(ClearTokens.Command::userId, userId)
                    .sample()

                // act
                shouldNotThrow<Exception> {
                    sut.invoke(command)
                }

                // assert
                refreshTokenRepository.find(userId) shouldBe null
            }
        }

        context("RefreshToken이 저장되어있는 경우") {
            it("삭제 처리된다.") {
                // arrange
                val userId = UUIDTypeId.random<User.Id>()
                val command = fixtureMonkey
                    .giveMeBuilder<ClearTokens.Command>()
                    .set(ClearTokens.Command::userId, userId)
                    .sample()
                val refreshToken = RefreshToken.generate(
                    secret = secret,
                    expirationSeconds = refreshTokenExpirationSeconds.toLong(),
                    userId = UUIDTypeId.random<User.Id>()
                )

                refreshTokenRepository.save(refreshToken, refreshTokenExpirationSeconds.toLong())

                // act
                sut.invoke(command)

                // assert
                refreshTokenRepository.find(userId) shouldBe null
            }
        }
    }

})

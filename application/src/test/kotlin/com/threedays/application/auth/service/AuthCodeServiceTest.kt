package com.threedays.application.auth.service

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.application.auth.port.inbound.VerifyAuthCode
import com.threedays.application.auth.port.outbound.AuthCodeSmsSenderSpy
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.repository.AuthCodeRepositorySpy
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.UserRepositorySpy
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@DisplayName("[서비스][인증] - AuthCode(인증 코드)")
class AuthCodeServiceTest : DescribeSpec({

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()

    val expirationSeconds = 300L

    val userRepository = UserRepositorySpy()
    val authCodeRepository = AuthCodeRepositorySpy()
    val authCodeSmsSender = AuthCodeSmsSenderSpy()
    val authProperties = AuthProperties(
        authCodeExpirationSeconds = expirationSeconds,
        tokenSecret = "tokenSecret"
    )
    val authCodeService = AuthCodeService(
        userRepository = userRepository,
        authCodeRepository = authCodeRepository,
        authCodeSmsSender = authCodeSmsSender,
        authProperties = authProperties
    )

    afterEach {
        authCodeRepository.clear()
        authCodeSmsSender.clear()
    }

    describe("인증 코드 생성 - create") {
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
                fixtureMonkey
                    .giveMeBuilder<User>()
                    .set(User::phoneNumber, phoneNumber)
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

    describe("인증 코드 검증 - verify") {

        context("인증코드가 만료되어 존재하지 않으면") {
            it("인증 코드 검증 시, AuthException.AuthCodeExpiredException 예외를 발생시킨다") {
                // arrange
                val authCode: AuthCode = AuthCode.create(
                    clientOS = ClientOS.AOS,
                    phoneNumber = PhoneNumber("01012345678"),
                    expireAt = LocalDateTime.now().minusSeconds(1)
                )
                authCodeRepository.save(authCode)

                val command: VerifyAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<VerifyAuthCode.Command>()
                    .set(VerifyAuthCode.Command::code, authCode.code.value)
                    .set(VerifyAuthCode.Command::id, authCode.id)
                    .sample()

                // act, assert
                shouldThrow<AuthException.AuthCodeExpiredException> { authCodeService.invoke(command) }
            }
        }

        context("인증코드가 유효하지 않으면") {
            it("인증 코드 검증 시, AuthException.InvalidAuthCodeException 예외를 발생시킨다") {
                // arrange
                val invalidCodeNumber = "000000"
                val authCode: AuthCode = AuthCode.create(
                    clientOS = ClientOS.AOS,
                    phoneNumber = PhoneNumber("01012345678"),
                    expireAt = LocalDateTime.now().plusSeconds(expirationSeconds)
                )
                authCodeRepository.save(authCode)

                val command: VerifyAuthCode.Command = fixtureMonkey
                    .giveMeBuilder<VerifyAuthCode.Command>()
                    .set(VerifyAuthCode.Command::code, invalidCodeNumber)
                    .set(VerifyAuthCode.Command::id, authCode.id)
                    .sample()

                // act, assert
                shouldThrow<AuthException.InvalidAuthCodeException> { authCodeService.invoke(command) }
            }
        }
    }

})

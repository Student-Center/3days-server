package com.threedays.application.auth.service

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.SendAuthCode
import com.threedays.application.auth.port.inbound.VerifyAuthCode
import com.threedays.application.auth.port.outbound.AuthCodeSmsSenderSpy
import com.threedays.application.auth.vo.command.AuthCodeCommand
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.entity.RegisterToken
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.repository.AuthCodeRepositorySpy
import com.threedays.domain.support.common.ClientOS
import com.threedays.support.common.security.jwt.JwtTokenProvider
import io.kotest.assertions.throwables.shouldNotThrowAny
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

    val authCodeRepository = AuthCodeRepositorySpy()
    val authCodeSmsSender = AuthCodeSmsSenderSpy()
    val authProperties = AuthProperties(
        authCodeExpirationSeconds = expirationSeconds,
        tokenSecret = "tokenSecret"
    )
    val authCodeService = AuthCodeService(
        authCodeRepository = authCodeRepository,
        authCodeSmsSender = authCodeSmsSender,
        authProperties = authProperties
    )

    afterEach {
        authCodeRepository.clear()
        authCodeSmsSender.clear()
    }

    describe("인증 코드 생성 - create") {
        it("새로운 인증코드를 생성하고, SMS를 발송한다") {
            // arrange
            val command: SendAuthCode.Command = fixtureMonkey
                .giveMeBuilder<SendAuthCode.Command>()
                .set(SendAuthCode.Command::phoneNumber, "01012345678")
                .sample()

            // act
            val authCode: AuthCode = authCodeService.invoke(command)

            // assert
            authCodeRepository.find(authCode.id) shouldBe authCode
            authCodeSmsSender.find(authCode.id) shouldBe authCode
        }
    }

    describe("인증 코드 검증 - verify") {

        context("인증코드가 만료되어 존재하지 않으면") {
            it("인증 코드 검증 시, AuthException.AuthCodeExpiredException 예외를 발생시킨다") {
                // arrange
                val authCode: AuthCode = AuthCode.create(
                    clientOS = ClientOS.AOS,
                    phoneNumber = "01012345678",
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
                    phoneNumber = "01012345678",
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

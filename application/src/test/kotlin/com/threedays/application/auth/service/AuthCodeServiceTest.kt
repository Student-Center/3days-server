package com.threedays.application.auth.service

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.outbound.AuthCodeSmsSenderSpy
import com.threedays.application.auth.vo.command.AuthCodeCommand
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.repository.AuthCodeRepositorySpy
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("[서비스] - 인증 코드")
class AuthCodeServiceTest : DescribeSpec({

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()

    val authCodeRepository = AuthCodeRepositorySpy()
    val authCodeSmsSender = AuthCodeSmsSenderSpy()
    val authProperties = AuthProperties(
        authCodeExpirationSeconds = 300L
    )
    val authCodeService = AuthCodeService(
        authCodeRepository = authCodeRepository,
        authCodeSmsSender = authCodeSmsSender,
        authProperties = authProperties
    )

    describe("인증 코드 생성") {
        it("새로운 인증코드를 생성하고, SMS를 발송한다") {
            // arrange
            val command: AuthCodeCommand.Create = fixtureMonkey
                .giveMeBuilder<AuthCodeCommand.Create>()
                .set(AuthCodeCommand.Create::phoneNumber, "01012345678")
                .sample()

            // act
            val authCode: AuthCode = authCodeService.create(command)

            // assert
            authCodeRepository.find(authCode.id) shouldBe authCode
            authCodeSmsSender.find(authCode.id) shouldBe authCode
        }
    }


})

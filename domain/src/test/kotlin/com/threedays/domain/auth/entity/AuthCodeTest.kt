package com.threedays.domain.auth.entity

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.support.common.ClientOS
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

@DisplayName("[도메인][인증] - 인증 코드")
class AuthCodeTest : DescribeSpec({

    val fixtureMonkey: FixtureMonkey = FixtureMonkey
        .builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()


    describe("인증 코드 생성") {
        it("새로운 인증 코드를 생성한다") {
            // arrange
            val phoneNumber = "01012345678"
            val expireAt: LocalDateTime = LocalDateTime.now().plusMinutes(5)
            val clientOS: ClientOS = fixtureMonkey.giveMeBuilder(ClientOS::class.java).sample()

            // act
            val authCode: AuthCode = AuthCode.create(clientOS, phoneNumber, expireAt)

            // assert
            authCode.id shouldNotBe null
            authCode.code.value.length shouldBe 6
            authCode.code.value.all { it.isDigit() } shouldBe true
            authCode.expireAt shouldBe expireAt
        }
    }

    describe("인증 코드 검증") {
        context("유효한 인증 코드로") {
            it("검증에 성공한다") {
                // arrange
                val phoneNumber = "01012345678"
                val expireAt: LocalDateTime = LocalDateTime.now().plusMinutes(5)
                val clientOS: ClientOS = fixtureMonkey.giveMeBuilder(ClientOS::class.java).sample()
                val authCode: AuthCode = AuthCode.create(clientOS, phoneNumber, expireAt)
                val validCode: AuthCode.Code = authCode.code

                // act & assert
                shouldNotThrow<AuthException> {
                    authCode.verify(validCode)
                }
            }
        }

        context("잘못된 인증 코드로") {
            it("InvalidAuthCodeException을 발생시킨다") {
                // arrange
                val phoneNumber = "01012345678"
                val expireAt: LocalDateTime = LocalDateTime.now().plusMinutes(5)
                val clientOS: ClientOS = fixtureMonkey.giveMeBuilder(ClientOS::class.java).sample()
                val authCode: AuthCode = AuthCode.create(clientOS, phoneNumber, expireAt)
                val invalidCode = AuthCode.Code("000000")

                // act & assert
                shouldThrow<AuthException.InvalidAuthCodeException> {
                    authCode.verify(invalidCode)
                }
            }
        }

        context("만료된 인증 코드로") {
            it("AuthCodeExpiredException을 발생시킨다") {
                // arrange
                val phoneNumber = "01012345678"
                val expireAt: LocalDateTime = LocalDateTime.now().minusMinutes(5)
                val clientOS: ClientOS = fixtureMonkey.giveMeBuilder(ClientOS::class.java).sample()
                val authCode: AuthCode = AuthCode.create(clientOS, phoneNumber, expireAt)

                // act & assert
                shouldThrow<AuthException.AuthCodeExpiredException> {
                    authCode.verify(authCode.code)
                }
            }
        }
    }

    describe("인증 코드 값 객체 (Code)") {
        context("유효한 코드로") {
            it("Code 객체를 생성한다") {
                // act
                val code = AuthCode.Code("123456")

                // assert
                code.value shouldBe "123456"
            }
        }

        context("6자리가 아닌 코드로") {
            it("예외를 발생시킨다") {
                // act & assert
                shouldThrow<IllegalArgumentException> {
                    AuthCode.Code("12345")
                }
                shouldThrow<IllegalArgumentException> {
                    AuthCode.Code("1234567")
                }
            }
        }

        context("숫자가 아닌 문자가 포함된 코드로") {
            it("예외를 발생시킨다") {
                // act & assert
                shouldThrow<IllegalArgumentException> {
                    AuthCode.Code("12345a")
                }
            }
        }

        context("코드 생성 시") {
            it("6자리 숫자로 구성된 코드를 생성한다") {
                // act
                val generatedCode = AuthCode.Code.generate()

                // assert
                generatedCode.value.length shouldBe 6
                generatedCode.value.all { it.isDigit() } shouldBe true
            }
        }
    }
})

package com.threedays.domain.auth.entity

import com.threedays.domain.auth.exception.AuthException
import com.threedays.support.common.security.jwt.JwtClaims
import com.threedays.support.common.security.jwt.JwtTokenProvider
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.*

@DisplayName("[도메인][Auth] - 회원 가입 토큰")
class RegisterTokenTest : DescribeSpec({

    describe("회원 가입 토큰 생성 - generate") {
        it("회원 가입 토큰을 생성한다") {
            // arrange
            val secret = "secret"

            // act
            val registerToken = RegisterToken.generate(secret)

            // assert
            registerToken.value.isNotBlank() shouldBe true

            val token: Result<JwtClaims> = JwtTokenProvider.verifyToken(registerToken.value, secret)

            token.isSuccess shouldBe true
            token.getOrNull()?.sub shouldBe "register"
        }
    }

    describe("회원 가입 토큰 검증 - verify") {
        context("회원 가입 토큰이 유효하면") {
            it("예외가 발생하지 않는다") {
                // arrange
                val secret = "secret"
                val registerToken = RegisterToken.generate(secret)

                // act, assert
                shouldNotThrowAny { RegisterToken.verify(registerToken.value, secret) }
            }
        }

        context("회원 가입 토큰이 만료되었으면") {
            it("AuthException.RegisterTokenExpiredException 예외가 발생한다") {
                // arrange
                val secret = "secret"
                val token = JwtTokenProvider.createToken(
                    JwtClaims {
                        registeredClaims {
                            sub = "register"
                            exp = Date(System.currentTimeMillis() - 1000)
                        }
                    },
                    secret
                )

                // act, assert
                shouldThrow<AuthException.RegisterTokenExpiredException> {
                    RegisterToken.verify(token, secret)
                }
            }
        }

        context("회원 가입 토큰이 JwtToken 형식이 아니면") {
            it("AuthException.InvalidRegisterTokenException 예외가 발생한다") {
                // arrange
                val secret = "secret"
                val invalidRegisterToken = "invalid"

                // act, assert
                shouldThrow<AuthException.InvalidRegisterTokenException> {
                    RegisterToken.verify(invalidRegisterToken, secret)
                }
            }
        }

        context("회원 가입 토큰의 sub가 register가 아니면") {
            it("AuthException.InvalidRegisterTokenException 예외가 발생한다") {
                // arrange
                val secret = "secret"
                val invalidToken: String = JwtTokenProvider.createToken(
                    JwtClaims {
                        registeredClaims {
                            sub = "invalid"
                        }
                    },
                    secret
                )

                // act, assert
                shouldThrow<AuthException.InvalidRegisterTokenException> {
                    RegisterToken.verify(invalidToken, secret)
                }
            }
        }

        context("회원 가입 토큰의 secret이 일치하지 않으면") {
            it("AuthException.InvalidRegisterTokenException 예외가 발생한다") {
                // arrange
                val secret = "secret"
                val registerToken = RegisterToken.generate(secret)
                val invalidSecret = "invalid"

                // act, assert
                shouldThrow<AuthException.InvalidRegisterTokenException> {
                    RegisterToken.verify(registerToken.value, invalidSecret)
                }
            }
        }
    }

})

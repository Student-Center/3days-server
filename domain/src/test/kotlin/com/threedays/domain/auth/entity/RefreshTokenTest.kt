package com.threedays.domain.auth.entity

import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.UUIDTypeId
import com.threedays.support.common.security.jwt.JwtClaims
import com.threedays.support.common.security.jwt.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.util.*

@DisplayName("[도메인][인증] - 리프레시 토큰")
class RefreshTokenTest : DescribeSpec({


    describe("RefreshToken") {
        val secret = "test-secret"
        val expirationSeconds = 3600L

        context("generate 메서드") {
            it("유효한 리프레시 토큰을 생성한다") {
                // Arrange
                val expectedSubject = "refresh"
                val userId: User.Id = UUIDTypeId.random<User.Id>()

                // Act
                val refreshToken = RefreshToken.generate(secret, userId, expirationSeconds)

                // Assert
                refreshToken.value.isNotBlank() shouldBe true
                JwtTokenProvider.verifyToken(refreshToken.value, secret)
                    .fold(
                        { claims ->
                            claims.sub shouldBe expectedSubject
                        },
                        { throw it }
                    )
            }
        }

        context("verify 메서드") {
            it("유효한 리프레시 토큰을 검증한다") {
                // Arrange
                val userId: User.Id = UUIDTypeId.random<User.Id>()
                val refreshToken = RefreshToken.generate(secret, userId, expirationSeconds)

                // Act
                val verifiedToken = RefreshToken.verify(refreshToken.value, secret)

                // Assert
                verifiedToken.value shouldBe refreshToken.value
            }

            it("만료된 리프레시 토큰에 대해 예외를 발생시킨다") {
                // Arrange
                val userId: User.Id = UUIDTypeId.random<User.Id>()
                val refreshToken = RefreshToken.generate(secret, userId, 0)

                // Act & Assert
                shouldThrow<AuthException.RefreshTokenExpiredException> {
                    RefreshToken.verify(refreshToken.value, secret)
                }
            }

            it("잘못된 서명의 리프레시 토큰에 대해 예외를 발생시킨다") {
                // Arrange
                val userId: User.Id = UUIDTypeId.random<User.Id>()
                val refreshToken = RefreshToken.generate(secret, userId, expirationSeconds)
                val wrongSecret = "wrong-secret"

                // Act & Assert
                shouldThrow<AuthException.InvalidRefreshTokenException> {
                    RefreshToken.verify(refreshToken.value, wrongSecret)
                }
            }

            it("잘못된 주제(subject)의 리프레시 토큰에 대해 예외를 발생시킨다") {
                // Arrange
                val wrongSubjectToken = JwtClaims {
                    registeredClaims {
                        sub = "wrong-subject"
                        exp = Date.from(Instant.now().plusSeconds(expirationSeconds))
                    }
                }.let { JwtTokenProvider.createToken(it, secret) }

                // Act & Assert
                shouldThrow<AuthException.InvalidRefreshTokenException> {
                    RefreshToken.verify(wrongSubjectToken, secret)
                }
            }
        }
    }


})

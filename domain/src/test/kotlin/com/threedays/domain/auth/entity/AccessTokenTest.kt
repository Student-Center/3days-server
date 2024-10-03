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

@DisplayName("[도메인][인증] - 액세스 토큰")
class AccessTokenTest : DescribeSpec({

    describe("AccessToken") {
        val secret = "test-secret"
        val userId: User.Id = UUIDTypeId.random<User.Id>()
        val expirationSeconds = 3600L

        context("generate 메서드") {
            it("유효한 액세스 토큰을 생성한다") {
                // Arrange
                val expectedSubject = "access"

                // Act
                val accessToken = AccessToken.generate(secret, userId, expirationSeconds)

                // Assert
                accessToken.userId shouldBe userId
                JwtTokenProvider.verifyToken(accessToken.value, secret)
                    .fold(
                        { claims ->
                            claims.sub shouldBe expectedSubject
                            claims.customClaims["userId"] shouldBe userId.value.toString()
                        },
                        { throw it }
                    )
            }
        }

        context("verify 메서드") {
            it("유효한 액세스 토큰을 검증한다") {
                // Arrange
                val accessToken = AccessToken.generate(secret, userId, expirationSeconds)

                // Act
                val verifiedToken = AccessToken.verify(accessToken.value, secret)

                // Assert
                verifiedToken.userId shouldBe userId
            }

            it("만료된 액세스 토큰에 대해 예외를 발생시킨다") {
                // Arrange
                val accessToken = AccessToken.generate(secret, userId, 0)

                // Act & Assert
                shouldThrow<AuthException.AccessTokenExpiredException> {
                    AccessToken.verify(accessToken.value, secret)
                }
            }

            it("잘못된 서명의 액세스 토큰에 대해 예외를 발생시킨다") {
                // Arrange
                val accessToken = AccessToken.generate(secret, userId, expirationSeconds)
                val wrongSecret = "wrong-secret"

                // Act & Assert
                shouldThrow<AuthException.InvalidAccessTokenException> {
                    AccessToken.verify(accessToken.value, wrongSecret)
                }
            }

            it("잘못된 주제(subject)의 액세스 토큰에 대해 예외를 발생시킨다") {
                // Arrange
                val wrongSubjectToken = JwtClaims {
                    registeredClaims {
                        sub = "wrong-subject"
                        exp = Date.from(Instant.now().plusSeconds(expirationSeconds))
                    }
                    customClaims {
                        put("userId", userId.value.toString())
                    }
                }.let { JwtTokenProvider.createToken(it, secret) }

                // Act & Assert
                shouldThrow<AuthException.InvalidAccessTokenException> {
                    AccessToken.verify(wrongSubjectToken, secret)
                }
            }
        }
    }


})

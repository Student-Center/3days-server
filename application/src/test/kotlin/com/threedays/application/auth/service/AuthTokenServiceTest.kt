package com.threedays.application.auth.service

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.IssueLoginTokens
import com.threedays.application.auth.port.inbound.RefreshLoginTokens
import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.repository.RefreshTokenRepositorySpy
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.UUIDTypeId
import com.threedays.support.common.security.jwt.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

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
            val user = fixtureMonkey
                .giveMeBuilder<User>()
                .set(User::id, userId)
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

})
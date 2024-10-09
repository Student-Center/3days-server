package com.threedays.application.auth.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.IssueLoginTokens
import com.threedays.application.auth.port.inbound.RefreshLoginTokens
import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.repository.RefreshTokenRepository
import com.threedays.domain.user.entity.User
import org.springframework.stereotype.Service

@Service
class AuthTokenService(
    private val authProperties: AuthProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) : IssueLoginTokens,
    RefreshLoginTokens {

    override fun invoke(command: IssueLoginTokens.Command): IssueLoginTokens.Result {
        val user: User = command.user
        val accessToken = generateAccessToken(user.id)
        val refreshToken = generateAndSaveRefreshToken(user.id)

        return IssueLoginTokens.Result(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = authProperties.accessTokenExpirationSeconds,
            refreshTokenExpiresIn = authProperties.refreshTokenExpirationSeconds
        )
    }

    override fun invoke(command: RefreshLoginTokens.Command): RefreshLoginTokens.Result {
        val refreshToken = verifyRefreshToken(command.refreshToken)
        val foundRefreshToken = refreshTokenRepository.find(refreshToken.userId)
            ?: throw AuthException.RefreshTokenExpiredException()

        if (foundRefreshToken.value != refreshToken.value) {
            throw AuthException.InvalidRefreshTokenException()
        }

        val accessToken = generateAccessToken(refreshToken.userId)
        val newRefreshToken = generateAndSaveRefreshToken(refreshToken.userId)

        return RefreshLoginTokens.Result(
            accessToken = accessToken.value,
            refreshToken = newRefreshToken.value,
            expiresIn = authProperties.accessTokenExpirationSeconds
        )
    }

    private fun generateAccessToken(userId: User.Id): AccessToken {
        return AccessToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.accessTokenExpirationSeconds,
            userId = userId
        )
    }

    private fun generateAndSaveRefreshToken(userId: User.Id): RefreshToken {
        return RefreshToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.refreshTokenExpirationSeconds,
            userId = userId
        ).also { refreshToken ->
            refreshTokenRepository.save(
                refreshToken = refreshToken,
                expiresIn = authProperties.refreshTokenExpirationSeconds
            )
        }
    }

    private fun verifyRefreshToken(value: String): RefreshToken {
        return RefreshToken.verify(
            value = value,
            secret = authProperties.tokenSecret
        )
    }
}

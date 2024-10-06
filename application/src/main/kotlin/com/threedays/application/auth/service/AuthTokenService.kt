package com.threedays.application.auth.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.IssueLoginTokens
import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.user.entity.User
import org.springframework.stereotype.Service

@Service
class AuthTokenService(
    private val authProperties: AuthProperties,
) : IssueLoginTokens {

    override fun invoke(command: IssueLoginTokens.Command): IssueLoginTokens.Result {
        val user: User = command.user

        val accessToken: AccessToken = AccessToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.accessTokenExpirationSeconds,
            userId = user.id
        )

        val refreshToken: RefreshToken = RefreshToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.refreshTokenExpirationSeconds,
            userId = user.id
        )

        return IssueLoginTokens.Result(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = authProperties.accessTokenExpirationSeconds,
            refreshTokenExpiresIn = authProperties.refreshTokenExpirationSeconds
        )
    }

}

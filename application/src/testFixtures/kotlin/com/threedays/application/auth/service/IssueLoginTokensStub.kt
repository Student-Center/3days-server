package com.threedays.application.auth.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.IssueLoginTokens
import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.entity.RefreshToken

class IssueLoginTokensStub(
    private val authProperties: AuthProperties
) : IssueLoginTokens {

    override fun invoke(command: IssueLoginTokens.Command): IssueLoginTokens.Result {
        val accessToken: AccessToken = AccessToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.accessTokenExpirationSeconds,
            userId = command.user.id
        )

        val refreshToken: RefreshToken = RefreshToken.generate(
            secret = authProperties.tokenSecret,
            expirationSeconds = authProperties.refreshTokenExpirationSeconds,
            userId = command.user.id
        )

        return IssueLoginTokens.Result(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = authProperties.accessTokenExpirationSeconds,
            refreshTokenExpiresIn = authProperties.refreshTokenExpirationSeconds
        )
    }

}

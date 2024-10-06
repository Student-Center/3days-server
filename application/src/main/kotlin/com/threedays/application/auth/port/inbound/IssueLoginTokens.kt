package com.threedays.application.auth.port.inbound

import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.user.entity.User

interface IssueLoginTokens {

    fun invoke(command: Command): Result

    data class Command(val user: User)

    data class Result(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken,
        val accessTokenExpiresIn: Long,
        val refreshTokenExpiresIn: Long
    )

}

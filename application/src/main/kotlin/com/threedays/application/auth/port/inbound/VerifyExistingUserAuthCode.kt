package com.threedays.application.auth.port.inbound

import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.entity.RefreshToken

interface VerifyExistingUserAuthCode {

    fun invoke(command: Command): Result

    data class Command(
        val id: AuthCode.Id,
        val code: AuthCode.Code,
    )

    data class Result(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken,
        val accessTokenExpiresIn: Long,
        val refreshTokenExpiresIn: Long
    )


}

package com.threedays.application.auth.port.inbound

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.entity.RegisterToken

interface VerifyNewUserAuthCode {

    fun invoke(command: Command): Result

    data class Command(
        val id: AuthCode.Id,
        val code: AuthCode.Code,
    )

    data class Result(
        val registerToken: RegisterToken,
        val expiresIn: Long
    )

}

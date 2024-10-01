package com.threedays.application.auth.port.inbound

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.entity.RegisterToken

interface VerifyAuthCode {

    fun invoke(command: Command): Result

    data class Command(
        val id: AuthCode.Id,
        val code: String,
    )

    sealed class Result {
        data class NewUser(val registerToken: RegisterToken): Result()
        data object ExistsUser: Result()
    }

}

package com.threedays.application.auth.port.inbound

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.support.common.ClientOS

interface SendAuthCode {

    fun invoke(command: Command): Result

    data class Command(
        val clientOS: ClientOS,
        val phoneNumber: String
    )

    sealed class Result{
        abstract val authCode: AuthCode

        data class ExistingUser(
            override val authCode: AuthCode
        ) : Result()

        data class NewUser(
            override val authCode: AuthCode
        ) : Result()
    }


}

package com.threedays.application.auth.port.inbound

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.support.common.ClientOS

interface SendAuthCode {

    fun invoke(command: Command): AuthCode

    data class Command(
        val clientOS: ClientOS,
        val phoneNumber: String
    )

}

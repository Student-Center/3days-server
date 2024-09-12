package com.threedays.application.auth.vo.command

import com.threedays.domain.support.common.ClientOS

sealed class AuthCodeCommand {

    data class Create(
        val clientOS: ClientOS,
        val phoneNumber: String
    ) : AuthCodeCommand()

}

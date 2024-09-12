package com.threedays.application.auth.port.inbound

import com.threedays.application.auth.vo.command.AuthCodeCommand
import com.threedays.domain.auth.entity.AuthCode

interface CommandAuthCode {

    fun create(command: AuthCodeCommand.Create): AuthCode

}

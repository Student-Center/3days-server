package com.threedays.application.auth.port.inbound

import com.threedays.application.auth.vo.command.AuthCodeCommand
import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.entity.RegisterToken

interface CommandAuthCode {

    fun create(command: AuthCodeCommand.Create): AuthCode
    fun verify(command: AuthCodeCommand.Verify): RegisterToken

}

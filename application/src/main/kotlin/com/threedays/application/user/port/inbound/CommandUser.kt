package com.threedays.application.user.port.inbound

import com.threedays.application.user.vo.command.UserCommand
import com.threedays.domain.user.entity.User

interface CommandUser {

    fun register(command: UserCommand.Register): User

}

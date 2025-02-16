package com.threedays.application.auth.port.inbound

import com.threedays.domain.user.entity.User

interface ClearTokens {

    fun invoke(command: Command)

    data class Command(val userId: User.Id)
}

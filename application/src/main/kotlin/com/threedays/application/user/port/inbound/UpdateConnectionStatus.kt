package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.User

fun interface UpdateConnectionStatus {

    operator fun invoke(command: Command): User

    data class Command(
        val userId: User.Id,
        val status: User.ConnectionStatus,
    )

}

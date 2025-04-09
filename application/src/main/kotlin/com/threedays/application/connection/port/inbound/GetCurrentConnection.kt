package com.threedays.application.connection.port.inbound

import com.threedays.domain.connection.entity.ConnectionAttempt
import com.threedays.domain.user.entity.User

interface GetCurrentConnectionAttempt {

    fun invoke(command: Command): Result

    data class Command(
        val userId: User.Id,
    )

    data class Result(
        val connectionAttempt: ConnectionAttempt,
    )
}

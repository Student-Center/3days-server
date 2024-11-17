package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.ProfileWidget
import com.threedays.domain.user.entity.User

interface DeleteProfileWidget {

    fun invoke(command: Command)

    data class Command(
        val userId: User.Id,
        val type: ProfileWidget.Type
    )

}

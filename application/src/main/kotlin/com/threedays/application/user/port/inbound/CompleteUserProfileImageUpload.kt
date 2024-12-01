package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserProfileImage

interface CompleteUserProfileImageUpload {

    fun invoke(command: Command)

    data class Command(
        val userId: User.Id,
        val imageId: UserProfileImage.Id,
        val extension: UserProfileImage.Extension,
    )

}

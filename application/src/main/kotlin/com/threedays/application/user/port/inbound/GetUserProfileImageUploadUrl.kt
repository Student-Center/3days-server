package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.UserProfileImage
import java.net.URL
import java.util.UUID

interface GetUserProfileImageUploadUrl {

    fun invoke(command: Command): Result

    data class Command(val extension: UserProfileImage.Extension)

    data class Result(
        val imageId: UUID,
        val extension: UserProfileImage.Extension,
        val url: URL,
        val uploadExpiresIn: Long
    )

}

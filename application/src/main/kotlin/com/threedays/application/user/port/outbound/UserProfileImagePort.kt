package com.threedays.application.user.port.outbound

import com.threedays.domain.user.entity.UserProfileImage
import java.net.URL
import java.util.UUID

interface UserProfileImagePort {

    fun getUploadUrl(
        id: UUID,
        extension: UserProfileImage.Extension,
        expiresIn: Long,        // seconds
        maxContentLength: Long, // bytes
    ): URL

    fun findImageUrlByIdAndExtension(
        id: UserProfileImage.Id,
        extension: UserProfileImage.Extension,
    ): URL

}

package com.threedays.domain.user.entity

import com.threedays.domain.user.exception.UserException
import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.UUIDTypeId
import io.github.oshai.kotlinlogging.KotlinLogging
import java.net.URL
import java.util.*

class UserProfileImage(
    override val id: Id,
    val extension: Extension,
    val url: URL,
) : DomainEntity<UserProfileImage, UserProfileImage.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    enum class Extension(val value: String) {
        PNG("png"),
    }

    companion object {

        private val logger = KotlinLogging.logger {}

        fun create(
            extension: Extension,
            getProfileImageUrlAction: (Id, Extension) -> URL,
        ): UserProfileImage {
            val imageId = Id(UUID.randomUUID())
            val imageUrl: URL = try {
                getProfileImageUrlAction(imageId, extension)
            } catch (e: Exception) {
                logger.error(e) { "Failed to upload profile image" }
                throw UserException.ProfileImageUploadFailedException()
            }

            return UserProfileImage(
                id = imageId,
                extension = extension,
                url = imageUrl,
            )
        }
    }


}

package com.threedays.application.user.service

import com.threedays.application.auth.config.UserProperties
import com.threedays.application.user.port.inbound.GetUserProfileImageUploadUrl
import com.threedays.application.user.port.inbound.CompleteUserProfileImageUpload
import com.threedays.application.user.port.inbound.DeleteUserProfileImage
import com.threedays.application.user.port.outbound.UserProfileImagePort
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.net.URL
import java.util.*

@Service
class UserProfileImageService(
    private val userRepository: UserRepository,
    private val userProfileImagePort: UserProfileImagePort,
    private val userProperties: UserProperties,
) : GetUserProfileImageUploadUrl, CompleteUserProfileImageUpload, DeleteUserProfileImage {

    override fun invoke(command: GetUserProfileImageUploadUrl.Command): GetUserProfileImageUploadUrl.Result {
        val imageId: UUID = UUID.randomUUID()
        val uploadUrl: URL = userProfileImagePort.getUploadUrl(
            id = imageId,
            extension = command.extension,
            expiresIn = userProperties.profileImage.uploadExpiresIn,
            maxContentLength = userProperties.profileImage.maxContentLength,
        )

        return GetUserProfileImageUploadUrl.Result(
            imageId = imageId,
            extension = command.extension,
            url = uploadUrl,
            uploadExpiresIn = userProperties.profileImage.uploadExpiresIn,
        )
    }

    override fun invoke(command: CompleteUserProfileImageUpload.Command) {
        userRepository
            .get(command.userId)
            .updateUserProfileImage(
                id = command.imageId,
                extension = command.extension,
                getProfileImageUrlAction = userProfileImagePort::findImageUrlByIdAndExtension,
            )
            .let { userRepository.save(it) }
    }

    override fun invoke(command: DeleteUserProfileImage.Command) {
        userRepository
            .get(command.userId)
            .deleteUserProfileImage(command.imageId, userProfileImagePort::deleteImageById)
            .let { userRepository.save(it) }
    }
}

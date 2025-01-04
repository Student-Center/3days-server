package com.threedays.bootstrap.api.user

import com.threedays.application.user.port.inbound.CompleteUserProfileImageUpload
import com.threedays.application.user.port.inbound.DeleteProfileWidget
import com.threedays.application.user.port.inbound.DeleteUserProfileImage
import com.threedays.application.user.port.inbound.GetUserProfileImageUploadUrl
import com.threedays.application.user.port.inbound.PutProfileWidget
import com.threedays.application.user.port.inbound.RegisterUser
import com.threedays.application.user.port.inbound.UpdateConnectionStatus
import com.threedays.application.user.port.inbound.UpdateDesiredPartner
import com.threedays.application.user.port.inbound.UpdateUserInfo
import com.threedays.bootstrap.api.support.security.UserAuthentication
import com.threedays.bootstrap.api.support.security.withUserAuthentication
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.entity.UserProfileImage
import com.threedays.domain.user.repository.UserRepository
import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobOccupation
import com.threedays.oas.api.UsersApi
import com.threedays.oas.model.CompleteProfileImageUploadRequest
import com.threedays.oas.model.GetMyUserInfoResponse
import com.threedays.oas.model.GetProfileImageUploadUrlResponse
import com.threedays.oas.model.ProfileImageExtension
import com.threedays.oas.model.ProfileWidget
import com.threedays.oas.model.ProfileWidgetType
import com.threedays.oas.model.RegisterUserRequest
import com.threedays.oas.model.TokenResponse
import com.threedays.oas.model.UpdateConnectionStatusRequest
import com.threedays.oas.model.UpdateConnectionStatusResponse
import com.threedays.oas.model.UpdateMyUserInfoRequest
import com.threedays.oas.model.UpdateMyUserInfoResponse
import com.threedays.oas.model.UpdateUserDesiredPartnerRequest
import com.threedays.oas.model.UpdateUserDesiredPartnerResponse
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.Year
import java.util.*

@RestController
class UserController(
    private val registerUser: RegisterUser,
    private val putProfileWidget: PutProfileWidget,
    private val userRepository: UserRepository,
    private val deleteProfileWidget: DeleteProfileWidget,
    private val updateUserInfo: UpdateUserInfo,
    private val updateDesiredPartner: UpdateDesiredPartner,
    private val getUserProfileImageUploadUrl: GetUserProfileImageUploadUrl,
    private val completeUserProfileImageUpload: CompleteUserProfileImageUpload,
    private val deleteUserProfileImage: DeleteUserProfileImage,
    private val updateConnectionStatus: UpdateConnectionStatus,
) : UsersApi {

    override fun registerUser(
        xRegisterToken: String,
        registerUserRequest: RegisterUserRequest,
    ): ResponseEntity<TokenResponse> {
        val result: RegisterUser.Result = RegisterUser.Command(
            name = User.Name(registerUserRequest.name),
            phoneNumber = PhoneNumber(registerUserRequest.phoneNumber),
            userGender = Gender.valueOf(registerUserRequest.profile.gender.name),
            userBirthYear = Year.of(registerUserRequest.profile.birthYear),
            userCompanyId = registerUserRequest.profile.companyId?.let { UUIDTypeId.from(it) },
            userJobOccupation = JobOccupation.valueOf(registerUserRequest.profile.jobOccupation.name),
            userLocationIds = registerUserRequest.profile.locationIds.map(UUIDTypeId::from),
            partnerJobOccupations = registerUserRequest.desiredPartner.jobOccupations
                .let { requestData -> requestData.map { JobOccupation.valueOf(it.name) } },
            partnerBirthYearRange = registerUserRequest.desiredPartner.birthYearRange
                .let { requestData ->
                    BirthYearRange(
                        start = requestData.start?.let { Year.of(it) },
                        end = requestData.end?.let { Year.of(it) }
                    )
                },
            partnerPreferDistance = UserDesiredPartner
                .PreferDistance
                .valueOf(registerUserRequest.desiredPartner.preferDistance.name),
            allowSameCompany = registerUserRequest.desiredPartner.allowSameCompany,
        ).let {
            registerUser.invoke(it)
        }

        return TokenResponse(
            result.accessToken.value,
            result.refreshToken.value,
            result.expiresIn.toInt()
        ).let {
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(it)
        }
    }

    override fun getMyUserInfo(): ResponseEntity<GetMyUserInfoResponse> =
        withUserAuthentication { userAuthentication ->
            val user: User = userRepository.get(userAuthentication.userId)

            GetMyUserInfoResponse(
                id = user.id.value,
                name = user.name.value,
                profileImages = user.profileImages.map(OASModelAdapter::toOASModel),
                phoneNumber = user.phoneNumber.value,
                profile = OASModelAdapter.toUserProfileDisplayInfo(user.profile),
                desiredPartner = OASModelAdapter.toOASModel(user.desiredPartner),
                profileWidgets = user.profile.profileWidgets.map(OASModelAdapter::toOASModel),
            ).let { ResponseEntity.ok(it) }
        }

    override fun putProfileWidget(body: ProfileWidget): ResponseEntity<ProfileWidget> =
        withUserAuthentication { userAuthentication: UserAuthentication ->
            val command = PutProfileWidget.Command(
                userId = userAuthentication.userId,
                profileWidget = OASModelAdapter.toDomainModel(body)
            )

            putProfileWidget
                .invoke(command)
                .let { ResponseEntity.ok(body) }
        }

    override fun deleteProfileWidget(type: ProfileWidgetType): ResponseEntity<Unit> =
        withUserAuthentication { authentication ->
            val command = DeleteProfileWidget.Command(
                userId = authentication.userId,
                type = com.threedays.domain.user.entity.ProfileWidget.Type.valueOf(type.name)
            )

            deleteProfileWidget.invoke(command)

            ResponseEntity.noContent().build()
        }

    override fun updateMyUserInfo(updateMyUserInfoRequest: UpdateMyUserInfoRequest): ResponseEntity<UpdateMyUserInfoResponse> =
        withUserAuthentication { authentication ->
            val command = UpdateUserInfo.Command(
                userId = authentication.userId,
                name = updateMyUserInfoRequest.name.let { User.Name(it) },
                jobOccupation = updateMyUserInfoRequest.jobOccupation.let {
                    JobOccupation.valueOf(it.name)
                },
                locationIds = updateMyUserInfoRequest.locationIds.map(UUIDTypeId::from),
                companyId = updateMyUserInfoRequest.companyId?.let { UUIDTypeId.from(it) },
                allowSameCompany = updateMyUserInfoRequest.allowSameCompany,
            )

            val user: User = updateUserInfo.invoke(command)

            UpdateMyUserInfoResponse(
                id = user.id.value,
                name = user.name.value,
                phoneNumber = user.phoneNumber.value,
                profile = OASModelAdapter.toOASModel(user.profile),
                desiredPartner = OASModelAdapter.toOASModel(user.desiredPartner)
            ).let { ResponseEntity.ok(it) }
        }

    override fun updateMyDesiredPartner(updateUserDesiredPartnerRequest: UpdateUserDesiredPartnerRequest): ResponseEntity<UpdateUserDesiredPartnerResponse> =
        withUserAuthentication { authentication ->
            val command = UpdateDesiredPartner.Command(
                userId = authentication.userId,
                jobOccupations = updateUserDesiredPartnerRequest.jobOccupations.map {
                    JobOccupation.valueOf(it.name)
                },
                birthYearRange = updateUserDesiredPartnerRequest.birthYearRange.let {
                    BirthYearRange(
                        start = it.start?.let { Year.of(it) },
                        end = it.end?.let { Year.of(it) }
                    )
                },
                preferDistance = UserDesiredPartner.PreferDistance.valueOf(
                    updateUserDesiredPartnerRequest.preferDistance.name
                ),
            )

            val user: User = updateDesiredPartner.invoke(command)

            UpdateUserDesiredPartnerResponse(
                desiredPartner = OASModelAdapter.toOASModel(user.desiredPartner)
            ).let { ResponseEntity.ok(it) }
        }

    override fun completeProfileImageUpload(completeProfileImageUploadRequest: CompleteProfileImageUploadRequest): ResponseEntity<Unit> =
        withUserAuthentication { authentication ->
            val command = CompleteUserProfileImageUpload.Command(
                userId = authentication.userId,
                imageId = UUIDTypeId.from(completeProfileImageUploadRequest.imageId),
                extension = UserProfileImage.Extension.valueOf(completeProfileImageUploadRequest.extension.name)
            )
            completeUserProfileImageUpload.invoke(command)
            ResponseEntity.ok().build()
        }

    override fun deleteProfileImage(imageId: UUID): ResponseEntity<Unit> =
        withUserAuthentication { authentication ->
            val command = DeleteUserProfileImage.Command(
                userId = authentication.userId,
                imageId = UUIDTypeId.from(imageId)
            )
            deleteUserProfileImage.invoke(command)
            ResponseEntity.noContent().build()
        }

    override fun getProfileImageUploadUrl(extension: ProfileImageExtension): ResponseEntity<GetProfileImageUploadUrlResponse> =
        withUserAuthentication { _ ->
            val command: GetUserProfileImageUploadUrl.Command =
                GetUserProfileImageUploadUrl.Command(
                    extension = UserProfileImage.Extension.valueOf(extension.name)
                )

            val result: GetUserProfileImageUploadUrl.Result =
                getUserProfileImageUploadUrl.invoke(command)

            GetProfileImageUploadUrlResponse(
                imageId = result.imageId,
                url = result.url.toString(),
                extension = ProfileImageExtension.valueOf(result.extension.name),
                uploadExpiresIn = result.uploadExpiresIn.toInt(),
            ).let {
                ResponseEntity.ok(it)
            }
        }

    override fun updateConnectionStatus(
        updateConnectionStatusRequest: UpdateConnectionStatusRequest
    ): ResponseEntity<UpdateConnectionStatusResponse> =
        withUserAuthentication { authentication ->
            val command = UpdateConnectionStatus.Command(
                userId = authentication.userId,
                status = User.ConnectionStatus.valueOf(updateConnectionStatusRequest.status.name)
            )

            val result: User = updateConnectionStatus(command)

            UpdateConnectionStatusResponse(
                status = UpdateConnectionStatusResponse.Status.valueOf(result.connectionStatus.name)
            ).let { ResponseEntity.ok(it) }
        }
}

package com.threedays.bootstrap.api.user

import com.threedays.application.user.port.inbound.CompleteUserProfileImageUpload
import com.threedays.application.user.port.inbound.DeleteProfileWidget
import com.threedays.application.user.port.inbound.DeleteUserProfileImage
import com.threedays.application.user.port.inbound.GetUserProfileImageUploadUrl
import com.threedays.application.user.port.inbound.PutProfileWidget
import com.threedays.application.user.port.inbound.RegisterUser
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
import com.threedays.oas.model.CompanyDisplayInfo
import com.threedays.oas.model.CompleteProfileImageUploadRequest
import com.threedays.oas.model.GetMyUserInfoResponse
import com.threedays.oas.model.GetProfileImageUploadUrlResponse
import com.threedays.oas.model.JobOccupationDisplayInfo
import com.threedays.oas.model.LocationDisplayInfo
import com.threedays.oas.model.PreferDistance
import com.threedays.oas.model.ProfileImage
import com.threedays.oas.model.ProfileImageExtension
import com.threedays.oas.model.ProfileWidget
import com.threedays.oas.model.ProfileWidgetType
import com.threedays.oas.model.RegisterUserRequest
import com.threedays.oas.model.TokenResponse
import com.threedays.oas.model.UpdateMyUserInfoRequest
import com.threedays.oas.model.UpdateMyUserInfoResponse
import com.threedays.oas.model.UpdateUserDesiredPartnerRequest
import com.threedays.oas.model.UpdateUserDesiredPartnerResponse
import com.threedays.oas.model.UserProfile
import com.threedays.oas.model.UserProfileDisplayInfo
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.Year
import java.util.UUID

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
) : UsersApi {

    override fun registerUser(
        xRegisterToken: String,
        registerUserRequest: RegisterUserRequest
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
                profileImages = user.profileImages.map {
                    ProfileImage(
                        id = it.id.value,
                        url = it.url.toURI(),
                        extension = ProfileImageExtension.valueOf(it.extension.name)
                    )
                },
                phoneNumber = user.phoneNumber.value,
                profile = UserProfileDisplayInfo(
                    gender = com.threedays.oas.model.Gender.valueOf(user.profile.gender.name),
                    birthYear = user.profile.birthYear.value,
                    jobOccupation = JobOccupationDisplayInfo(
                        code = com.threedays.oas.model.JobOccupation.valueOf(user.profile.jobOccupation.name),
                        display = "test"
                    ),
                    locations = user.profile.locations.map {
                        LocationDisplayInfo(
                            id = it.id.value,
                            display = it.display,
                        )
                    },
                    company = user.profile.company?.let {
                        CompanyDisplayInfo(
                            id = it.id.value,
                            display = it.display,
                        )
                    },
                ),
                desiredPartner = com.threedays.oas.model.UserDesiredPartner(
                    jobOccupations = user.desiredPartner.jobOccupations.map {
                        com.threedays.oas.model.JobOccupation.valueOf(
                            it.name
                        )
                    },
                    birthYearRange = user.desiredPartner.birthYearRange.let {
                        com.threedays.oas.model.BirthYearRange(
                            it.start?.value,
                            it.end?.value,
                        )
                    },
                    preferDistance = PreferDistance.valueOf(user.desiredPartner.preferDistance.name),
                ),
                profileWidgets = user.profile.profileWidgets.map {
                    ProfileWidget(
                        type = ProfileWidgetType.valueOf(it.type.name),
                        content = it.content,
                    )
                }
            ).let { ResponseEntity.ok(it) }
        }

    override fun putProfileWidget(body: ProfileWidget): ResponseEntity<ProfileWidget> =
        withUserAuthentication { userAuthentication: UserAuthentication ->
            val command = PutProfileWidget.Command(
                userId = userAuthentication.userId,
                profileWidget = com.threedays.domain.user.entity.ProfileWidget(
                    type = com.threedays.domain.user.entity.ProfileWidget.Type.valueOf(body.type.name),
                    content = body.content
                )
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
                    JobOccupation.valueOf(
                        it.name
                    )
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
                profile = UserProfile(
                    gender = com.threedays.oas.model.Gender.valueOf(user.profile.gender.name),
                    birthYear = user.profile.birthYear.value,
                    jobOccupation = com.threedays.oas.model.JobOccupation.valueOf(user.profile.jobOccupation.name),
                    locationIds = user.profile.locations.map { it.id.value },
                    companyId = user.profile.company?.id?.value,
                ),
                desiredPartner = com.threedays.oas.model.UserDesiredPartner(
                    jobOccupations = user.desiredPartner.jobOccupations.map {
                        com.threedays.oas.model.JobOccupation.valueOf(
                            it.name
                        )
                    },
                    birthYearRange = user.desiredPartner.birthYearRange.let {
                        com.threedays.oas.model.BirthYearRange(
                            it.start?.value,
                            it.end?.value,
                        )
                    },
                    preferDistance = PreferDistance.valueOf(user.desiredPartner.preferDistance.name),
                )
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
                preferDistance = UserDesiredPartner.PreferDistance.valueOf(updateUserDesiredPartnerRequest.preferDistance.name),
            )

            val user: User = updateDesiredPartner.invoke(command)

            UpdateUserDesiredPartnerResponse(
                desiredPartner = com.threedays.oas.model.UserDesiredPartner(
                    birthYearRange = user.desiredPartner.birthYearRange.let {
                        com.threedays.oas.model.BirthYearRange(
                            it.start?.value,
                            it.end?.value,
                        )
                    },
                    jobOccupations = user.desiredPartner.jobOccupations.map {
                        com.threedays.oas.model.JobOccupation.valueOf(it.name)
                    },
                    preferDistance = PreferDistance.valueOf(user.desiredPartner.preferDistance.name),
                )
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
            val command: GetUserProfileImageUploadUrl.Command = GetUserProfileImageUploadUrl.Command(
                extension = UserProfileImage.Extension.valueOf(extension.name)
            )

            val result: GetUserProfileImageUploadUrl.Result = getUserProfileImageUploadUrl.invoke(command)

            GetProfileImageUploadUrlResponse(
                imageId = result.imageId,
                url = result.url.toString(),
                extension = ProfileImageExtension.valueOf(result.extension.name),
                uploadExpiresIn = result.uploadExpiresIn.toInt(),
            ).let {
                ResponseEntity.ok(it)
            }
        }
}

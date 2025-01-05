package com.threedays.domain.user.entity

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.entity.UserProfileImage.Extension
import com.threedays.domain.user.repository.CompanyQueryRepository
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobOccupation
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import com.threedays.support.common.exception.NotFoundException
import java.net.URL
import java.time.Year
import java.util.*

/**
 * User Entity
 */
data class User(
    override val id: Id,
    val name: Name,
    val phoneNumber: PhoneNumber,
    val profileImages: List<UserProfileImage> = emptyList(),
    val profile: UserProfile,
    val desiredPartner: UserDesiredPartner,
    val connectionStatus: ConnectionStatus,
) : AggregateRoot<User, User.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    @JvmInline
    value class Name(val value: String) {

        init {
            require(value.isNotBlank()) { "이름은 공백일 수 없습니다." }
        }
    }

    enum class ConnectionStatus { ACTIVE, INACTIVE }

    init {
        if (profile.company == null) {
            require(desiredPartner.allowSameCompany == null) {
                "회사를 선택하지 않았다면 동일 회사 허용 여부를 선택할 수 없습니다."
            }
        }

        if (desiredPartner.allowSameCompany != null) {
            require(profile.company != null) {
                "동일 회사 허용 여부를 선택했다면 회사를 선택해야 합니다."
            }
        }
    }

    companion object {

        fun create(
            name: Name,
            phoneNumber: PhoneNumber,
            userGender: Gender,
            userBirthYear: Year,
            userCompany: Company?,
            userJobOccupation: JobOccupation,
            userLocations: List<Location>,
            partnerBirthYearRange: BirthYearRange,
            partnerJobOccupations: List<JobOccupation>,
            partnerPreferDistance: PreferDistance,
            allowSameCompany: Boolean?,
            connectionStatus: ConnectionStatus = ConnectionStatus.ACTIVE,
        ): User {
            val id: Id = UUIDTypeId.random<Id>()

            val profile = UserProfile(
                id = id,
                gender = userGender,
                birthYear = userBirthYear,
                company = userCompany,
                jobOccupation = userJobOccupation,
                locations = userLocations,
            )

            val desiredPartner = UserDesiredPartner(
                id = id,
                birthYearRange = partnerBirthYearRange,
                jobOccupations = partnerJobOccupations,
                preferDistance = partnerPreferDistance,
                allowSameCompany = allowSameCompany,
            )

            return User(
                id = id,
                name = name,
                phoneNumber = phoneNumber,
                profile = profile,
                desiredPartner = desiredPartner,
                connectionStatus = connectionStatus,
            )
        }
    }

    fun putProfileWidget(widget: ProfileWidget): User {
        return copy(profile = profile.putProfileWidget(widget))
    }

    fun deleteProfileWidget(type: ProfileWidget.Type): User {
        return copy(profile = profile.deleteProfileWidget(type))
    }

    fun updateUserInfo(
        name: Name?,
        jobOccupation: JobOccupation,
        locationIds: List<Location.Id>,
        locationQueryRepository: LocationQueryRepository,
        companyId: Company.Id?,
        companyQueryRepository: CompanyQueryRepository,
        allowSameCompany: Boolean?,
    ): User {
        val locations: List<Location> = locationIds.map { locationQueryRepository.get(it) }
        val company: Company? = companyId?.let { companyQueryRepository.get(it) }

        return copy(
            name = name ?: this.name,
            profile = profile.copy(
                jobOccupation = jobOccupation,
                locations = locations,
                company = company,
            ),
            desiredPartner = desiredPartner.copy(
                allowSameCompany = allowSameCompany ?: desiredPartner.allowSameCompany,
            )
        )
    }

    fun updateDesiredPartner(
        birthYearRange: BirthYearRange,
        jobOccupations: List<JobOccupation>,
        preferDistance: PreferDistance,
    ): User {
        return copy(
            desiredPartner = desiredPartner.copy(
                birthYearRange = birthYearRange,
                jobOccupations = jobOccupations,
                preferDistance = preferDistance,
            )
        )
    }

    // TODO: 이미지 여러개 업로드 가능하도록 수정 필요
    fun updateUserProfileImage(
        id: UserProfileImage.Id,
        extension: Extension,
        getProfileImageUrlAction: (UserProfileImage.Id, Extension) -> URL,
    ): User {
        val newProfileImage = UserProfileImage(
            id = id,
            extension = extension,
            url = getProfileImageUrlAction(id, extension),
        )
        return copy(profileImages = listOf(newProfileImage))
    }

    fun deleteUserProfileImage(
        imageId: UserProfileImage.Id,
        deleteProfileImageAction: (UserProfileImage.Id) -> Unit,
    ): User {
        val targetProfileImage: UserProfileImage = profileImages.find { it.id == imageId }
            ?: throw NotFoundException("해당 프로필 이미지를 찾을 수 없습니다.")

        deleteProfileImageAction(targetProfileImage.id)

        val updatedProfileImages: List<UserProfileImage> = profileImages.filter { it.id != imageId }

        return copy(profileImages = updatedProfileImages)
    }

    fun updateConnectionStatus(connectionStatus: ConnectionStatus): User {
        return copy(connectionStatus = connectionStatus)
    }

}

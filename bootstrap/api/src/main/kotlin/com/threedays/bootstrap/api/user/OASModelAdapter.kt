package com.threedays.bootstrap.api.user

import com.threedays.domain.user.entity.UserProfileImage
import com.threedays.oas.model.BirthYearRange
import com.threedays.oas.model.CompanyDisplayInfo
import com.threedays.oas.model.JobOccupation
import com.threedays.oas.model.JobOccupationDisplayInfo
import com.threedays.oas.model.LocationDisplayInfo
import com.threedays.oas.model.PreferDistance
import com.threedays.oas.model.ProfileImage
import com.threedays.oas.model.ProfileImageExtension
import com.threedays.oas.model.ProfileWidget
import com.threedays.oas.model.ProfileWidgetType
import com.threedays.oas.model.UserDesiredPartner
import com.threedays.oas.model.UserProfileDisplayInfo
import com.threedays.oas.model.Gender
import com.threedays.oas.model.UserProfile

fun UserProfileImage.toOASModel(): ProfileImage = ProfileImage(
    id = this.id.value,
    url = this.url.toURI(),
    extension = ProfileImageExtension.valueOf(this.extension.name)
)

fun com.threedays.domain.user.entity.UserProfile.toUserProfileDisplayInfo(): UserProfileDisplayInfo =
    UserProfileDisplayInfo(
        gender = Gender.valueOf(this.gender.name),
        birthYear = this.birthYear.value,
        jobOccupation = JobOccupationDisplayInfo(
            code = JobOccupation.valueOf(this.jobOccupation.name),
            display = this.jobOccupation.koreanName
        ),
        locations = this.locations.map {
            LocationDisplayInfo(
                id = it.id.value,
                display = it.display,
            )
        },
        company = this.company?.let {
            CompanyDisplayInfo(
                id = it.id.value,
                display = it.display,
            )
        },
    )

fun com.threedays.domain.user.entity.UserProfile.toOASModel() = UserProfile(
    gender = Gender.valueOf(this.gender.name),
    birthYear = this.birthYear.value,
    jobOccupation = JobOccupation.valueOf(this.jobOccupation.name),
    locationIds = this.locations.map { it.id.value },
    companyId = this.company?.id?.value,
)

fun com.threedays.domain.user.entity.ProfileWidget.toOASModel() = ProfileWidget(
    type = ProfileWidgetType.valueOf(this.type.name),
    content = this.content,
)

fun ProfileWidget.toDomainModel() = com.threedays.domain.user.entity.ProfileWidget(
    type = com.threedays.domain.user.entity.ProfileWidget.Type.valueOf(this.type.name),
    content = this.content,
)

fun com.threedays.domain.user.entity.UserDesiredPartner.toOASModel() = UserDesiredPartner(
    jobOccupations = this.jobOccupations.map {
        JobOccupation.valueOf(it.name)
    },
    birthYearRange = this.birthYearRange.let {
        BirthYearRange(
            start = it.start?.value,
            end = it.end?.value,
        )
    },
    preferDistance = PreferDistance.valueOf(this.preferDistance.name),
)

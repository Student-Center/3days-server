package com.threedays.bootstrap.api.user

import com.threedays.oas.model.BirthYearRange
import com.threedays.oas.model.CompanyDisplayInfo
import com.threedays.oas.model.Gender
import com.threedays.oas.model.JobOccupation
import com.threedays.oas.model.JobOccupationDisplayInfo
import com.threedays.oas.model.LocationDisplayInfo
import com.threedays.oas.model.PreferDistance
import com.threedays.oas.model.ProfileImage
import com.threedays.oas.model.ProfileImageExtension
import com.threedays.oas.model.ProfileWidget
import com.threedays.oas.model.ProfileWidgetType
import com.threedays.oas.model.UserDesiredPartner
import com.threedays.oas.model.UserProfile
import com.threedays.oas.model.UserProfileDisplayInfo

object OASModelAdapter {
    fun toOASModel(domainModel: com.threedays.domain.user.entity.UserProfileImage): ProfileImage = ProfileImage(
        id = domainModel.id.value,
        url = domainModel.url.toURI(),
        extension = ProfileImageExtension.valueOf(domainModel.extension.name),
    )

    fun toUserProfileDisplayInfo(domainModel: com.threedays.domain.user.entity.UserProfile): UserProfileDisplayInfo =
        UserProfileDisplayInfo(
            gender = Gender.valueOf(domainModel.gender.name),
            birthYear = domainModel.birthYear.value,
            jobOccupation = JobOccupationDisplayInfo(
                code = JobOccupation.valueOf(domainModel.jobOccupation.name),
                display = domainModel.jobOccupation.koreanName
            ),
            locations = domainModel.locations.map {
                LocationDisplayInfo(
                    id = it.id.value,
                    display = it.display,
                )
            },
            company = domainModel.company?.let {
                CompanyDisplayInfo(
                    id = it.id.value,
                    display = it.display,
                )
            },
        )

    fun toOASModel(domainModel: com.threedays.domain.user.entity.UserProfile) = UserProfile(
        gender = Gender.valueOf(domainModel.gender.name),
        birthYear = domainModel.birthYear.value,
        jobOccupation = JobOccupation.valueOf(domainModel.jobOccupation.name),
        locationIds = domainModel.locations.map { it.id.value },
        companyId = domainModel.company?.id?.value,
    )

    fun toOASModel(domainModel: com.threedays.domain.user.entity.ProfileWidget) = ProfileWidget(
        type = ProfileWidgetType.valueOf(domainModel.type.name),
        content = domainModel.content,
    )

    fun toDomainModel(oasModel: ProfileWidget) = com.threedays.domain.user.entity.ProfileWidget(
        type = com.threedays.domain.user.entity.ProfileWidget.Type.valueOf(oasModel.type.name),
        content = oasModel.content,
    )

    fun toOASModel(domainModel: com.threedays.domain.user.entity.UserDesiredPartner) = UserDesiredPartner(
        jobOccupations = domainModel.jobOccupations.map {
            JobOccupation.valueOf(it.name)
        },
        birthYearRange = domainModel.birthYearRange.let {
            BirthYearRange(
                start = it.start?.value,
                end = it.end?.value,
            )
        },
        preferDistance = PreferDistance.valueOf(domainModel.preferDistance.name),
    )
}
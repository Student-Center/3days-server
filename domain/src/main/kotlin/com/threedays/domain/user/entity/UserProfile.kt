package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobOccupation
import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserProfile(
    override val id: User.Id,
    val gender: Gender,
    val birthYear: Year,
    val company: Company? = null,
    val jobOccupation: JobOccupation,
    val locations: List<Location>,
    val profileWidgets: List<ProfileWidget> = emptyList(),
) : DomainEntity<UserProfile, User.Id>() {

    fun putProfileWidget(profileWidget: ProfileWidget): UserProfile {
        val existingProfileWidget: ProfileWidget? =
            profileWidgets.find { it.type == profileWidget.type }

        return if (existingProfileWidget == null) {
            copy(profileWidgets = profileWidgets + profileWidget)
        } else {
            copy(profileWidgets = profileWidgets.map { if (it.type == profileWidget.type) profileWidget else it })
        }
    }

}

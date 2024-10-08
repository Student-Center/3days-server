package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.Gender
import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserProfile(
    override val id: User.Id,
    val gender: Gender,
    val birthYear: Year,
    val company: Company,
    val jobOccupation: JobOccupation,
    val locations: List<Location>
) : DomainEntity<UserProfile, User.Id>()

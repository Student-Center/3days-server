package com.threedays.domain.user.entity

import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserProfile(
    override val id: com.threedays.domain.user.vo.UserId,
    val gender: com.threedays.domain.user.vo.Gender,
    val birthYear: Year,
    val company: com.threedays.domain.user.vo.Company,
    val job: com.threedays.domain.user.vo.Job,
    val locations: List<com.threedays.domain.user.vo.Location>
) : DomainEntity<com.threedays.domain.user.entity.UserProfile, com.threedays.domain.user.vo.UserId>()

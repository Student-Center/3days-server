package com.sc.weave2.domain.user.entity

import com.sc.weave2.domain.user.vo.Company
import com.sc.weave2.domain.user.vo.Gender
import com.sc.weave2.domain.user.vo.Job
import com.sc.weave2.domain.user.vo.Location
import com.sc.weave2.domain.user.vo.UserId
import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserProfile(
    override val id: UserId,
    val gender: Gender,
    val birthYear: Year,
    val company: Company,
    val job: Job,
    val locations: List<Location>
) : DomainEntity<UserProfile, UserId>()

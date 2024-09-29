package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.CompanyId
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobId
import com.threedays.domain.user.vo.LocationId
import com.threedays.domain.user.vo.UserId
import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserProfile(
    override val id: UserId,
    val gender: Gender,
    val birthYear: Year,
    val companyId: CompanyId,
    val jobId: JobId,
    val locationIds: List<LocationId>
) : DomainEntity<UserProfile, UserId>()

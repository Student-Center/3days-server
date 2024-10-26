package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.JobOccupation
import com.threedays.support.common.base.domain.DomainEntity

data class UserDesiredPartner(
    override val id: User.Id,
    val birthYearRange: BirthYearRange,
    val jobOccupations: List<JobOccupation>,
    val preferDistance: PreferDistance,
    val allowSameCompany: Boolean? = null,
) : DomainEntity<UserDesiredPartner, User.Id>() {

    enum class PreferDistance {
        ONLY_MY_AREA,
        INCLUDE_SURROUNDING_REGIONS,
        ANYWHERE
    }

}

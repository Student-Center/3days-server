package com.threedays.domain.user.entity

import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserDesiredPartner(
    override val id: User.Id,
    val birthYearRange: ClosedRange<Year>? = null,
    val jobOccupations: List<JobOccupation>,
    val preferDistance: PreferDistance
) : DomainEntity<UserDesiredPartner, User.Id>() {

    enum class PreferDistance {
        ONLY_MY_AREA,
        INCLUDE_SURROUNDING_REGIONS,
        ANYWHERE
    }

}

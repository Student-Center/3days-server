package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.UserId
import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserDesiredPartner(
    override val id: UserId,
    val birthYearRange: ClosedRange<Year>,
    val jobOccupations: List<Job.Occupation>,
    val preferDistance: PreferDistance
) : DomainEntity<UserDesiredPartner, UserId>() {

    enum class PreferDistance {
        ONLY_MY_AREA,
        INCLUDE_SURROUNDING_REGIONS,
        ANYWHERE
    }

}

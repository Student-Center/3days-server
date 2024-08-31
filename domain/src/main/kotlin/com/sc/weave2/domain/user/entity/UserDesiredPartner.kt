package com.sc.weave2.domain.user.entity

import com.sc.weave2.domain.user.vo.Job
import com.sc.weave2.domain.user.vo.UserId
import com.threedays.support.common.base.domain.DomainEntity
import java.time.Year

data class UserDesiredPartner(
    override val id: UserId,
    val ageRange: ClosedRange<Year>,
    val job: Job,
    val preferDistance: PreferDistance
) : DomainEntity<UserDesiredPartner, UserId>() {

    enum class PreferDistance {
        ONLY_MY_AREA,
        INCLUDE_SURROUNDING_REGIONS,
        ANYWHERE
    }


}

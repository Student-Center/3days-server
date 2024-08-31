package com.sc.weave2.domain.user.entity

import com.sc.weave2.domain.user.vo.UserId
import com.threedays.support.common.base.domain.AggregateRoot


data class User(
    override val id: UserId,
    val name: Name,
    val profile: UserProfile,
    val desiredPartner: UserDesiredPartner,
) : AggregateRoot<User, UserId>() {

    @JvmInline
    value class Name(val value: String)


}

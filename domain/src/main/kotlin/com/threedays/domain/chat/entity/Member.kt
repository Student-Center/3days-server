package com.threedays.domain.chat.entity

import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.DomainEntity
import java.time.LocalDateTime

data class Member(
    override val id: User.Id,
    val joinedAt: LocalDateTime = LocalDateTime.now()
) : DomainEntity<Member, User.Id>()

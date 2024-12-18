package com.threedays.domain.chat.entity

import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.TypeId
import java.time.LocalDateTime
import java.util.*

data class ChatMember(
    override val id: Id,
    val userId: User.Id,
    val joinedAt: LocalDateTime = LocalDateTime.now()
) : DomainEntity<ChatMember, ChatMember.Id>() {

    data class Id(override val value: UUID) : TypeId<UUID>(value)

}

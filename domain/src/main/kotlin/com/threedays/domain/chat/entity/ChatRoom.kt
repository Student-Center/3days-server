package com.threedays.domain.chat.entity

import com.threedays.domain.connection.entity.Connection
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.TypeId
import java.time.LocalDateTime
import java.util.UUID

data class ChatRoom(
    override val id: Id,
    val connectionId: Connection.Id,
    val members: MutableList<ChatMember> = mutableListOf(),
    val createdAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<ChatRoom, ChatRoom.Id>() {

    data class Id(override val value: UUID) : TypeId<UUID>(value)


}

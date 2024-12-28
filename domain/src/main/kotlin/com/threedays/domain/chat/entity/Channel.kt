package com.threedays.domain.chat.entity

import com.threedays.domain.connection.entity.Connection
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.TypeId
import java.time.LocalDateTime
import java.util.*

data class Channel(
    override val id: Id,
    val connectionId: Connection.Id,
    val members: List<Member>,
    val createdAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<Channel, Channel.Id>() {

    data class Id(override val value: UUID) : TypeId<UUID>(value)

    companion object {

        fun create(connectionId: Connection.Id): Channel {
            return Channel(
                id = Id(UUID.randomUUID()),
                connectionId = connectionId,
                members = emptyList()
            )
        }

    }

}

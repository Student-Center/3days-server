package com.threedays.domain.connection.entity

import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import java.time.LocalDateTime
import java.util.*

data class Connection(
    override val id: Id,
    val participant1: Participant,
    val participant2: Participant,
    val connectedAt: LocalDateTime,
) : AggregateRoot<Connection, Connection.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    companion object {

        fun match(
            user1: User.Id,
            user2: User.Id
        ): Connection {
            return Connection(
                id = UUIDTypeId.random(),
                participant1 = Participant(user1),
                participant2 = Participant(user2),
                connectedAt = LocalDateTime.now()
            )
        }
    }


}

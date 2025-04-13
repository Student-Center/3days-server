package com.threedays.domain.connection.entity

import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.UUIDTypeId
import java.time.LocalDateTime
import java.util.UUID

data class Connection(
    override val id: Id,
    val participant1: Participant,
    val participant2: Participant,
    val connectedAt: LocalDateTime,
    val cancellation: ConnectionCancellation?,
) : DomainEntity<Connection, Connection.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    fun cancel(
        userId: User.Id,
        reason: ConnectionCancellation.Reason,
        detail: String? = null,
    ): Connection {
        val connectionCancellation = ConnectionCancellation.create(
            userId = userId,
            reason = reason,
            detail = detail,
        )
        return copy(
            cancellation = connectionCancellation
        )
    }

    fun isParticipant(userId: User.Id): Boolean {
        return participant1.id == userId || participant2.id == userId
    }

    companion object {

        fun match(
            user1: User,
            user2: User,
        ): Connection {
            return Connection(
                id = UUIDTypeId.random(),
                participant1 = Participant.from(user1),
                participant2 = Participant.from(user2),
                connectedAt = LocalDateTime.now(),
                cancellation = null,
            )
        }
    }
}

package com.threedays.persistence.connection.entity

import com.threedays.domain.connection.entity.ConnectionCancellation
import com.threedays.domain.user.entity.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "connection_cancellation")
class ConnectionCancellationJpaEntity(
    id: UUID,
    userId: UUID,
    reason: String,
    detail: String?,
    createdAt: LocalDateTime,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "user_id", nullable = false)
    var userId: UUID = userId
        private set

    @Column(name = "reason", nullable = false)
    var reason: String = reason
        private set

    @Column(name = "detail", nullable = true)
    var detail: String? = detail
        private set

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = createdAt
        private set


    fun toDomain(): ConnectionCancellation {
        return ConnectionCancellation(
            id = ConnectionCancellation.Id(id),
            userId = User.Id(userId),
            reason = ConnectionCancellation.Reason.from(reason),
            detail = detail,
            createdAt = createdAt,
        )
    }

    companion object {

        fun from(domain: ConnectionCancellation): ConnectionCancellationJpaEntity {
            return ConnectionCancellationJpaEntity(
                id = domain.id.value,
                userId = domain.userId.value,
                reason = domain.reason.name,
                detail = domain.detail,
                createdAt = domain.createdAt
            )
        }
    }
}

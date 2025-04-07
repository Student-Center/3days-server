package com.threedays.persistence.connection.entity

import com.threedays.domain.connection.entity.ConnectionAttempt
import com.threedays.domain.user.entity.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "connection_attempt")
class ConnectionAttemptJpaEntity(
    id: UUID,
    userId: UUID,
    connection: ConnectionJpaEntity?,
    status: String,
    attemptDate: LocalDate,
    createdAt: LocalDateTime,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "user_id", nullable = false)
    var userId: UUID = userId
        private set

    @OneToOne(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
    )
    var connection: ConnectionJpaEntity? = connection
        private set

    @Column(name = "status", nullable = false)
    var status: String = status
        private set

    @Column(name = "attempt_date", nullable = false)
    var attemptDate: LocalDate = attemptDate
        private set

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = createdAt
        private set

    companion object {
        fun ConnectionAttempt.toJpaEntity() = ConnectionAttemptJpaEntity(
            id = id.value,
            userId = userId.value,
            connection = connection?.let { ConnectionJpaEntity.from(it) },
            status = status.name,
            attemptDate = attemptDate,
            createdAt = createdAt
        )
    }

    fun toDomainEntity() = ConnectionAttempt(
        id = ConnectionAttempt.Id(id),
        userId = User.Id(userId),
        connection = connection?.toDomain(),
        status = ConnectionAttempt.Status.from(status),
        attemptDate = attemptDate,
        createdAt = createdAt,
    )
}

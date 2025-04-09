package com.threedays.persistence.connection.entity

import com.threedays.domain.connection.entity.Connection
import com.threedays.domain.connection.entity.Participant
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "connection")
class ConnectionJpaEntity(
    id: UUID,
    participant1Id: UUID,
    participant2Id: UUID,
    connectedAt: LocalDateTime,
    cancellation: ConnectionCancellationJpaEntity?,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "participant1_id", nullable = false)
    var participant1Id: UUID = participant1Id
        private set

    @Column(name = "participant2_id", nullable = false)
    var participant2Id: UUID = participant2Id
        private set

    @Column(name = "connected_at", nullable = false)
    var connectedAt: LocalDateTime = connectedAt
        private set

    @OneToOne(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
    )
    var cancellation: ConnectionCancellationJpaEntity? = cancellation
        private set


    fun toDomain(participant1: Participant, participant2: Participant): Connection {
        return Connection(
            id = Connection.Id(id),
            participant1 = participant1,
            participant2 = participant2,
            connectedAt = connectedAt,
            cancellation = cancellation?.toDomain(),
        )
    }

    companion object {
        fun from(domain: Connection): ConnectionJpaEntity {
            return ConnectionJpaEntity(
                id = domain.id.value,
                participant1Id = domain.participant1.id.value,
                participant2Id = domain.participant2.id.value,
                connectedAt = domain.connectedAt,
                cancellation = domain.cancellation?.let { ConnectionCancellationJpaEntity.from(it) },
            )
        }
    }
}


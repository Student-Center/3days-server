package com.threedays.persistence.connection.entity

import com.threedays.domain.connection.entity.Connection
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "connection")
class ConnectionJpaEntity(
    id: UUID,
    participant1: ParticipantJpaEntity,
    participant2: ParticipantJpaEntity,
    connectedAt: LocalDateTime,
) {

    @Id
    var id: UUID = id
        private set

    @OneToOne(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
    )
    var participant1: ParticipantJpaEntity = participant1
        private set

    @OneToOne(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
    )
    var participant2: ParticipantJpaEntity = participant2
        private set

    @Column(name = "connected_at", nullable = false)
    var connectedAt: LocalDateTime = connectedAt
        private set

    fun toDomain(): Connection {
        return Connection(
            id = Connection.Id(id),
            participant1 = participant1.toDomain(),
            participant2 = participant2.toDomain(),
            connectedAt = connectedAt
        )
    }

    companion object {

        fun from(domain: Connection): ConnectionJpaEntity {
            return ConnectionJpaEntity(
                id = domain.id.value,
                participant1 = ParticipantJpaEntity.from(domain.participant1),
                participant2 = ParticipantJpaEntity.from(domain.participant2),
                connectedAt = domain.connectedAt
            )
        }
    }
}

package com.threedays.persistence.connection.entity

import com.threedays.domain.connection.entity.Participant
import com.threedays.domain.user.entity.User
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "participant")
class ParticipantJpaEntity(
    id: UUID,
    connectionResponse: Participant.ConnectionResponse
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "connection_response", nullable = false)
    @Enumerated(EnumType.STRING)
    var connectionResponse: Participant.ConnectionResponse = connectionResponse
        private set

    fun toDomain(): Participant {
        return Participant(
            id = User.Id(id),
            connectionResponse = connectionResponse
        )
    }

    companion object {
        fun from(domain: Participant): ParticipantJpaEntity {
            return ParticipantJpaEntity(
                id = domain.id.value,
                connectionResponse = domain.connectionResponse
            )
        }
    }
}

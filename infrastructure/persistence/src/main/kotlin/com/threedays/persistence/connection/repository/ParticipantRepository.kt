package com.threedays.persistence.connection.repository

import com.threedays.persistence.connection.entity.ParticipantEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ParticipantRepository {
    fun getAllParticipants(participantIds: Set<UUID>): Map<UUID, ParticipantEntity>
}

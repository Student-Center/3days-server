package com.threedays.persistence.connection.repository

import com.threedays.persistence.connection.entity.ParticipantEntity
import com.threedays.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ParticipantRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : ParticipantRepository {
    override fun getAllParticipants(participantIds: Set<UUID>): Map<UUID, ParticipantEntity> {
        return userJpaRepository.findAllById(participantIds).associate { it.id to ParticipantEntity(it) }
    }
}

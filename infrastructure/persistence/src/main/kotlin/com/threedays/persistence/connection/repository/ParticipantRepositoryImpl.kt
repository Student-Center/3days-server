package com.threedays.persistence.connection.repository

import com.threedays.persistence.connection.entity.ParticipantEntity
import com.threedays.persistence.user.repository.UserJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ParticipantRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : ParticipantRepository {
    override fun getAllParticipants(participantIds: Set<UUID>): Map<UUID, ParticipantEntity> {
        val users = userJpaRepository.findAllById(participantIds)
        if (users.size != participantIds.size) {
            val foundIds = users.map { it.id }.toSet()
            val missingIds = participantIds - foundIds
            throw IllegalStateException("일부 참가자를 찾을 수 없습니다: $missingIds")
        }
        return users.associate { it.id to ParticipantEntity(it) }
    }
}

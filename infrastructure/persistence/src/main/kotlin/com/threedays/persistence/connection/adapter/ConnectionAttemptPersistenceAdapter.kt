package com.threedays.persistence.connection.adapter

import com.threedays.domain.connection.entity.ConnectionAttempt
import com.threedays.domain.connection.repository.ConnectionAttemptRepository
import com.threedays.domain.user.entity.User
import com.threedays.persistence.connection.entity.ConnectionAttemptJpaEntity.Companion.toJpaEntity
import com.threedays.persistence.connection.repository.ConnectionAttemptJpaRepository
import com.threedays.persistence.connection.repository.ParticipantRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class ConnectionAttemptPersistenceAdapter(
    private val participantRepository: ParticipantRepository,
    private val connectionAttemptJpaRepository: ConnectionAttemptJpaRepository,
) : ConnectionAttemptRepository {
    override fun findLatestConnectionAttempt(userId: User.Id): ConnectionAttempt? {
        return connectionAttemptJpaRepository.findFirstByUserIdOrderByCreatedAtDesc(userId.value)?.let { entity ->
            val connectionJpaEntity = entity.connection
            if (connectionJpaEntity != null) {
                val participantIds = setOf(connectionJpaEntity.participant1Id, connectionJpaEntity.participant2Id)
                val idToParticipant = participantRepository.getAllParticipants(participantIds)
                val participant1 = idToParticipant[connectionJpaEntity.participant1Id]!!.toDomain()
                val participant2 = idToParticipant[connectionJpaEntity.participant2Id]!!.toDomain()
                entity.toDomainEntity(connectionJpaEntity.toDomain(participant1, participant2))
            } else {
                entity.toDomainEntity()
            }
        }
    }

    override fun save(root: ConnectionAttempt) {
        connectionAttemptJpaRepository.save(root.toJpaEntity())
    }

    override fun find(id: ConnectionAttempt.Id): ConnectionAttempt? {
        return connectionAttemptJpaRepository.findById(id.value).getOrNull()?.toDomainEntity()
    }

    override fun delete(id: ConnectionAttempt.Id) {
        throw NotImplementedError()
    }

    override fun delete(root: ConnectionAttempt) {
        throw NotImplementedError()
    }


}

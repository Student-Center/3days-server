package com.threedays.persistence.connection.adapter

import com.threedays.domain.connection.entity.ConnectionAttempt
import com.threedays.domain.connection.repository.ConnectionAttemptRepository
import com.threedays.persistence.connection.entity.ConnectionAttemptJpaEntity.Companion.toJpaEntity
import com.threedays.persistence.connection.repository.ConnectionAttemptJpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
@Transactional
class ConnectionAttemptPersistenceAdapter(
    private val connectionAttemptJpaRepository: ConnectionAttemptJpaRepository,
) : ConnectionAttemptRepository {
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

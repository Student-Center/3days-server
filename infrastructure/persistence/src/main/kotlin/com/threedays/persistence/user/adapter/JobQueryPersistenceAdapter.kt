package com.threedays.persistence.user.adapter

import com.threedays.domain.user.entity.Job
import com.threedays.domain.user.repository.JobQueryRepository
import com.threedays.persistence.user.repository.JobJpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class JobQueryPersistenceAdapter(
    private val jobJpaRepository: JobJpaRepository
) : JobQueryRepository {

    override fun find(id: Job.Id): Job? {
        return jobJpaRepository
            .findById(id.value)
            .map { it.toDomainEntity() }
            .orElse(null)
    }

}

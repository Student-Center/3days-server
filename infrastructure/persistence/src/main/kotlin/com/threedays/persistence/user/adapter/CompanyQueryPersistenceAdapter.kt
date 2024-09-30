package com.threedays.persistence.user.adapter

import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.repository.CompanyQueryRepository
import com.threedays.persistence.user.repository.CompanyJpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class CompanyQueryPersistenceAdapter(
    private val companyJpaRepository: CompanyJpaRepository
) : CompanyQueryRepository {

    override fun find(id: Company.Id): Company? {
        return companyJpaRepository
            .findById(id.value)
            .map { it.toDomainEntity() }
            .orElse(null)
    }

}

package com.threedays.persistence.user.adapter

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.repository.CompanyQueryRepository
import com.threedays.persistence.user.entity.CompanyJpaEntity
import com.threedays.persistence.user.repository.CompanyJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class CompanyQueryPersistenceAdapter(
    private val companyJpaRepository: CompanyJpaRepository,
    private val entityManager: EntityManager,
    private val jdslRenderContext: RenderContext,
) : CompanyQueryRepository {

    override fun searchCompanies(
        name: String,
        next: Company.Id?,
        limit: Int
    ): Pair<List<Company>, Company.Id?> {
        val query: SelectQuery<CompanyJpaEntity> = jpql {
            select(
                entity(CompanyJpaEntity::class)
            ).from(
                entity(CompanyJpaEntity::class)
            ).whereAnd(
                path(CompanyJpaEntity::name).like("%$name%"),
                next?.let { path(CompanyJpaEntity::id).greaterThanOrEqualTo(it.value) }
            )
        }

        val result: List<Company> = entityManager
            .createQuery(query, jdslRenderContext)
            .apply { maxResults = limit + 1 }
            .resultList
            .map { it.toDomainEntity() }

        val hasNextPage: Boolean = result.size > limit
        val nextId: Company.Id? = if (hasNextPage) {
            result[limit].id
        } else {
            null
        }

        return result.take(limit) to nextId
    }

    override fun find(id: Company.Id): Company? {
        return companyJpaRepository
            .findById(id.value)
            .map { it.toDomainEntity() }
            .orElse(null)
    }

}

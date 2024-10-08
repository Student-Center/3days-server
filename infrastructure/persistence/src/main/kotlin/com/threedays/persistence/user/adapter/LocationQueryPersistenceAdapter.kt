package com.threedays.persistence.user.adapter

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.persistence.user.entity.LocationJpaEntity
import com.threedays.persistence.user.repository.LocationJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class LocationQueryPersistenceAdapter(
    private val locationJpaRepository: LocationJpaRepository,
    private val entityManager: EntityManager,
    private val jdslRenderContext: RenderContext,
) : LocationQueryRepository {

    override fun searchLocations(
        name: String,
        next: Location.Id?,
        limit: Int
    ): Pair<List<Location>, Location.Id?> {

        val query: SelectQuery<LocationJpaEntity> = jpql {
            select(
                entity(LocationJpaEntity::class)
            ).from(
                entity(LocationJpaEntity::class)
            ).whereAnd(
                or(
                    path(LocationJpaEntity::region).like("%$name%"),
                    path(LocationJpaEntity::subRegion).like("%$name%")
                ),
                next?.let { path(LocationJpaEntity::id).gt(it.value) }
            )
        }

        val result = entityManager
            .createQuery(query, jdslRenderContext)
            .apply { setMaxResults(limit + 1) }
            .resultList
            .map { it.toDomainEntity() }

        val nextId = result.getOrNull(limit)?.id ?: next

        return result.take(limit) to nextId
    }


    override fun find(id: Location.Id): Location? = locationJpaRepository
        .findById(id.value)
        .map { it.toDomainEntity() }
        .orElse(null)

}

package com.threedays.persistence.user.adapter

import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.persistence.user.repository.LocationJpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class LocationQueryPersistenceAdapter(
    private val locationJpaRepository: LocationJpaRepository
) : LocationQueryRepository {

    override fun findAll(): List<Location> =
        locationJpaRepository
            .findAll()
            .map { it.toDomainEntity() }

    override fun find(id: Location.Id): Location? = locationJpaRepository
        .findById(id.value)
        .map { it.toDomainEntity() }
        .orElse(null)

}

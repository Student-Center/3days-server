package com.threedays.persistence.user.adapter

import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.domain.user.vo.LocationId
import com.threedays.persistence.user.entity.LocationEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class LocationQueryPersistenceAdapter : LocationQueryRepository {
    override fun findAll(): List<Location> = LocationEntity
        .selectAll()
        .toList()
        .map { it.toLocation() }

    override fun find(id: LocationId): Location? = LocationEntity
        .selectAll()
        .where { LocationEntity.id eq id.value }
        .singleOrNull()
        ?.toLocation()
}

private fun ResultRow.toLocation() = Location(
    id = LocationId(this[LocationEntity.id].value),
    region = Location.Region(this[LocationEntity.region]),
    subRegion = Location.SubRegion(this[LocationEntity.subRegion]),
)

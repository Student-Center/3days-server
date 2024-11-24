package com.threedays.domain.user.reposiotry

import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.repository.LocationQueryRepository

class LocationQueryRepositorySpy: LocationQueryRepository {

    private val locations = mutableListOf<Location>()

    fun save(location: Location) {
        locations.add(location)
    }

    fun clear() {
        locations.clear()
    }

    override fun getLocationRegions(): List<String> {
        return locations.map { it.region.value }.distinct()
    }

    override fun getLocationsByRegion(regionName: String): List<Location> {
        return locations.filter { it.region.value == regionName }
    }

    override fun find(id: Location.Id): Location? {
        return locations.find { it.id == id }
    }
}

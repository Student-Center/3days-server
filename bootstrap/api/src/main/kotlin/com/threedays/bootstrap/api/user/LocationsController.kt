package com.threedays.bootstrap.api.user

import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.oas.api.LocationsApi
import com.threedays.oas.model.Location
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationsController(
    private val locationQueryRepository: LocationQueryRepository,
) : LocationsApi {

    override fun getLocationRegions(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(locationQueryRepository.getLocationRegions())
    }

    override fun getLocationsByRegion(regionName: String): ResponseEntity<List<Location>> {
        return locationQueryRepository
            .getLocationsByRegion(regionName)
            .map {
                Location(
                    id = it.id.value,
                    region = it.region.value,
                    subRegion = it.subRegion.value,
                )
            }
            .let { ResponseEntity.ok(it) }
    }

}

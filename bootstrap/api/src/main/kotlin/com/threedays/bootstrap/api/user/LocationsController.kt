package com.threedays.bootstrap.api.user

import com.threedays.application.user.port.inbound.QueryLocation
import com.threedays.oas.api.LocationsApi
import com.threedays.oas.model.Location
import com.threedays.oas.model.OSType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationsController(
    private val queryLocation: QueryLocation,
) : LocationsApi {

    override fun getLocations(): ResponseEntity<List<Location>> {
        return queryLocation
            .findAll()
            .map {
                Location(
                    id = it.id.value,
                    region = it.region.value,
                    subRegion = it.subRegion.value,
                )
            }
            .toList()
            .let { ResponseEntity.ok(it) }
    }

}

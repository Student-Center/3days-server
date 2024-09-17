package com.threedays.bootstrap.api.location

import com.threedays.application.user.port.inbound.QueryLocation
import com.threedays.oas.api.LocationsApi
import com.threedays.oas.model.Location
import com.threedays.oas.model.OSType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class LocationsController(
    private val queryLocation: QueryLocation,
) : LocationsApi {
    override fun locationsGet(xOSType: OSType): ResponseEntity<List<Location>> {
        val locations = queryLocation.findAll().map {
            Location(
                id = it.id.value,
                region = it.region.value,
                subRegion = it.subRegion.value,
            )
        }.toList()
        return ResponseEntity.ok(locations)
    }

}

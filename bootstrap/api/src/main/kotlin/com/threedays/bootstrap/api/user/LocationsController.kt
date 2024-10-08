package com.threedays.bootstrap.api.user

import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.oas.api.LocationsApi
import com.threedays.oas.model.Location
import com.threedays.oas.model.SearchLocationsResponse
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class LocationsController(
    private val locationQueryRepository: LocationQueryRepository,
) : LocationsApi {

    override fun searchLocations(
        name: String,
        next: UUID?,
        limit: Int
    ): ResponseEntity<SearchLocationsResponse> {
        val (resultLocation, resultNext) = locationQueryRepository.searchLocations(
            name = name,
            next = next?.let { UUIDTypeId.from(it) },
            limit = limit
        )

        val locationResponse: List<Location> = resultLocation.map {
            Location(
                id = it.id.value,
                region = it.region.value,
                subRegion = it.subRegion.value,
            )
        }

        return ResponseEntity.ok(
            SearchLocationsResponse(
                locations = locationResponse,
                next = resultNext?.value
            )
        )
    }

}

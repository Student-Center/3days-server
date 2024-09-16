package com.threedays.application.user.service

import com.threedays.application.user.port.inbound.QueryLocation
import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.repository.LocationQueryRepository
import org.springframework.stereotype.Service

@Service
class LocationService(
    private val locationQueryRepository: LocationQueryRepository,
) : QueryLocation {

    override fun findAll(): List<Location> {
        return locationQueryRepository.findAll()
    }

}

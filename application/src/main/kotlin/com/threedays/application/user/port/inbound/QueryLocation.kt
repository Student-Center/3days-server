package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.Location

interface QueryLocation {

    fun findAll(): List<Location>

}

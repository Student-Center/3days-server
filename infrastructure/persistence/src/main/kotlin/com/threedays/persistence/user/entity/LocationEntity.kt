package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object LocationEntity : UUIDTable("locations") {
    val region = varchar("region", 255)
    var subRegion = varchar("sub_region", 255)
}

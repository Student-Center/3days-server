package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object LocationEntity : UUIDTable("locations") {
    val name = varchar("name", 255)
}

package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object CompanyEntity : UUIDTable("companies") {
    val name = varchar("name", 255)
}

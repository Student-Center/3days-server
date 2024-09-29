package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object JobEntity: UUIDTable("jobs") {
    val occupation = varchar("occupation", 50)
    val name = varchar("name", 255)
}

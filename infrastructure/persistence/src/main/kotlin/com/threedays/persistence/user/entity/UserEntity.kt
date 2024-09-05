package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object UserEntity : UUIDTable("users") {

    val name = varchar("name", 255)
}

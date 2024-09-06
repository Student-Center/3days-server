package com.threedays.persistence.user.entity

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserLocationEntity : Table("user_locations") {
    val userId = reference("user_id", UserEntity, onDelete = ReferenceOption.CASCADE)
    val locationId = reference("location_id", LocationEntity, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(userId, locationId)
}

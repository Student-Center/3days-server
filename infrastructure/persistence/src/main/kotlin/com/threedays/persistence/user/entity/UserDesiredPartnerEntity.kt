package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object UserDesiredPartnerEntity : UUIDTable("user_desired_partners") {
    val birthYearRangeStart = integer("birth_year_range_start")
    val birthYearRangeEnd = integer("birth_year_range_end")
    val preferDistance = varchar("prefer_distance", 50)
}

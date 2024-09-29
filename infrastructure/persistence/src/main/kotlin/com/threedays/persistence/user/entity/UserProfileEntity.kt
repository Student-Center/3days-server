package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object UserProfileEntity : UUIDTable("user_profiles") {
    val gender = varchar("gender", 50)
    val birthYear = integer("birth_year")
    val companyId = uuid("company_id")
    val jobId = uuid("job_id")
}

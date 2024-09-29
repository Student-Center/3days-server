package com.threedays.persistence.user.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object UserDesiredPartnerJobOccupationEntity: UUIDTable("user_desired_partner_job_occupations") {
    val userId = reference("user_id", UserEntity.id)
    val jobOccupation = varchar("job_occupation", 50)
}

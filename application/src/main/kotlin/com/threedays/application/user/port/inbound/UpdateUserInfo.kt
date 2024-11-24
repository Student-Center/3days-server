package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.vo.JobOccupation

interface UpdateUserInfo {

    fun invoke(command: Command): User

    data class Command(
        val userId: User.Id,
        val name: User.Name,
        val jobOccupation: JobOccupation,
        val locationIds: List<Location.Id>,
        val companyId: Company.Id? = null,
        val allowSameCompany: Boolean? = null,
    )
}

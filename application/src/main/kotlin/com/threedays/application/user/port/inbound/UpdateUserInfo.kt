package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.vo.JobOccupation

interface UpdateUserInfo {

    fun invoke(command: Command): User

    data class Command(
        val userId: User.Id,
        val name: User.Name? = null,
        val jobOccupation: JobOccupation? = null,
        val locationIds: List<Location.Id>? = null,
    )
}

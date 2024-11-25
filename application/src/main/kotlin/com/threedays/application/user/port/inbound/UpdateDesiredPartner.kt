package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.JobOccupation

interface UpdateDesiredPartner {

    fun invoke(command: Command): User

    data class Command(
        val userId: User.Id,
        val birthYearRange: BirthYearRange,
        val jobOccupations: List<JobOccupation>,
        val preferDistance: UserDesiredPartner.PreferDistance,
    )

}

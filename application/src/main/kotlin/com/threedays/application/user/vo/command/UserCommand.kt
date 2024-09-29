package com.threedays.application.user.vo.command

import com.threedays.domain.user.entity.Job
import com.threedays.domain.user.entity.User.Name
import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.CompanyId
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobId
import com.threedays.domain.user.vo.LocationId
import java.time.Year

sealed class UserCommand {

    data class Register(
        val name: Name,
        val userGender: Gender,
        val userBirthYear: Year,
        val userCompanyId: CompanyId,
        val userJobId: JobId,
        val userLocationIds: List<LocationId>,
        val partnerBirthYearRange: ClosedRange<Year>,
        val partnerJobOccupations: List<Job.Occupation>,
        val partnerPreferDistance: PreferDistance
    )

}

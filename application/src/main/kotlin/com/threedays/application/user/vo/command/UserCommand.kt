package com.threedays.application.user.vo.command

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.entity.Job
import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.entity.User.Name
import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.Gender
import java.time.Year

sealed class UserCommand {

    data class Register(
        val name: Name,
        val phoneNumber: PhoneNumber,
        val userGender: Gender,
        val userBirthYear: Year,
        val userCompanyId: Company.Id,
        val userJobId: Job.Id,
        val userLocationIds: List<Location.Id>,
        val partnerBirthYearRange: ClosedRange<Year>,
        val partnerJobOccupations: List<Job.Occupation>,
        val partnerPreferDistance: PreferDistance
    )

}

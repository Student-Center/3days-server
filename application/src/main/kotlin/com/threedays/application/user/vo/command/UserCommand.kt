package com.threedays.application.user.vo.command

import com.threedays.domain.user.entity.User.Name
import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.Company
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.Job
import com.threedays.domain.user.vo.Location
import java.time.Year

sealed class UserCommand {

    data class Register(
        val name: Name,
        val userGender: Gender,
        val userBirthYear: Year,
        val userCompany: Company,
        val userJob: Job,
        val userLocations: List<Location>,
        val partnerBirthYearRange: ClosedRange<Year>,
        val partnerJob: Job,
        val partnerPreferDistance: PreferDistance
    )
}

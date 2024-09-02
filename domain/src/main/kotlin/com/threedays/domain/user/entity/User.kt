package com.threedays.domain.user.entity

import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.Company
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.Job
import com.threedays.domain.user.vo.Location
import com.threedays.domain.user.vo.UserId
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import java.time.Year


data class User(
    override val id: UserId,
    val name: Name,
    val profile: UserProfile,
    val desiredPartner: UserDesiredPartner,
) : AggregateRoot<User, UserId>() {

    @JvmInline
    value class Name(val value: String)

    companion object {

        fun create(
            name: Name,
            userGender: Gender,
            userBirthYear: Year,
            userCompany: Company,
            userJob: Job,
            userLocations: List<Location>,
            partnerBirthYearRange: ClosedRange<Year>,
            partnerJob: Job,
            partnerPreferDistance: PreferDistance
        ): User {
            val id: UserId = UUIDTypeId.random<UserId>()

            val profile = UserProfile(
                id = id,
                gender = userGender,
                birthYear = userBirthYear,
                company = userCompany,
                job = userJob,
                locations = userLocations,
            )

            val desiredPartner = UserDesiredPartner(
                id = id,
                birthYearRange = partnerBirthYearRange,
                job = partnerJob,
                preferDistance = partnerPreferDistance,
            )

            return User(
                id = id,
                name = name,
                profile = profile,
                desiredPartner = desiredPartner,
            )
        }
    }

}

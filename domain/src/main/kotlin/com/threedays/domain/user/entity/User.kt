package com.threedays.domain.user.entity

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.Gender
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import java.time.Year
import java.util.*


data class User(
    override val id: Id,
    val name: Name,
    val phoneNumber: PhoneNumber,
    val profile: UserProfile,
    val desiredPartner: UserDesiredPartner,
) : AggregateRoot<User, User.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    @JvmInline
    value class Name(val value: String)

    companion object {

        fun create(
            name: Name,
            phoneNumber: PhoneNumber,
            userGender: Gender,
            userBirthYear: Year,
            userCompany: Company,
            userJobOccupation: JobOccupation,
            userLocations: List<Location>,
            partnerBirthYearRange: ClosedRange<Year>?,
            partnerJobOccupations: List<JobOccupation>,
            partnerPreferDistance: PreferDistance
        ): User {
            val id: Id = UUIDTypeId.random<Id>()

            val profile = UserProfile(
                id = id,
                gender = userGender,
                birthYear = userBirthYear,
                company = userCompany,
                jobOccupation = userJobOccupation,
                locations = userLocations,
            )

            val desiredPartner = UserDesiredPartner(
                id = id,
                birthYearRange = partnerBirthYearRange,
                jobOccupations = partnerJobOccupations,
                preferDistance = partnerPreferDistance,
            )

            return User(
                id = id,
                name = name,
                phoneNumber = phoneNumber,
                profile = profile,
                desiredPartner = desiredPartner,
            )
        }
    }

}

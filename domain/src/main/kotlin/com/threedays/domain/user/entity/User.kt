package com.threedays.domain.user.entity

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobOccupation
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

    init {
        if (profile.company == null) {
            require(desiredPartner.allowSameCompany == null) {
                "회사를 선택하지 않았다면 동일 회사 허용 여부를 선택할 수 없습니다."
            }
        }

        if (desiredPartner.allowSameCompany != null) {
            require(profile.company != null) {
                "동일 회사 허용 여부를 선택했다면 회사를 선택해야 합니다."
            }
        }
    }

    companion object {

        fun create(
            name: Name,
            phoneNumber: PhoneNumber,
            userGender: Gender,
            userBirthYear: Year,
            userCompany: Company?,
            userJobOccupation: JobOccupation,
            userLocations: List<Location>,
            partnerBirthYearRange: BirthYearRange,
            partnerJobOccupations: List<JobOccupation>,
            partnerPreferDistance: PreferDistance,
            allowSameCompany: Boolean?,
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
                allowSameCompany = allowSameCompany,
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

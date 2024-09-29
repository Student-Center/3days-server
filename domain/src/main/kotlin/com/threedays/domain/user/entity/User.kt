package com.threedays.domain.user.entity

import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.CompanyId
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobId
import com.threedays.domain.user.vo.LocationId
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
            userCompanyId: CompanyId,
            userJobId: JobId,
            userLocationIds: List<LocationId>,
            partnerBirthYearRange: ClosedRange<Year>,
            partnerJobOccupations: List<Job.Occupation>,
            partnerPreferDistance: PreferDistance
        ): User {
            val id: UserId = UUIDTypeId.random<UserId>()

            val profile = UserProfile(
                id = id,
                gender = userGender,
                birthYear = userBirthYear,
                companyId = userCompanyId,
                jobId = userJobId,
                locationIds = userLocationIds,
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
                profile = profile,
                desiredPartner = desiredPartner,
            )
        }
    }

}

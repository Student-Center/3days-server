package com.threedays.persistence.user.entity

import com.threedays.domain.user.entity.Job
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.time.Year
import java.util.*

@Entity
@Table(name = "user_desired_partners")
class UserDesiredPartnerJpaEntity(
    id: UUID,
    birthYearRangeStart: Int,
    birthYearRangeEnd: Int,
    jobOccupations: List<Job.Occupation>,
    preferDistance: UserDesiredPartner.PreferDistance,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "birth_year_range_start", nullable = false, updatable = false)
    var birthYearRangeStart: Int = birthYearRangeStart
        private set

    @Column(name = "birth_year_range_end", nullable = false, updatable = false)
    var birthYearRangeEnd: Int = birthYearRangeEnd
        private set

    @ElementCollection(
        fetch = FetchType.EAGER,
        targetClass = Job.Occupation::class,
    )
    @CollectionTable(
        name = "user_desired_partner_job_occupations",
        joinColumns = [JoinColumn(name = "user_desired_partner_id")],
    )
    @Enumerated(EnumType.STRING)
    var jobOccupations: List<Job.Occupation> = jobOccupations
        private set

    @Column(
        name = "prefer_distance",
        nullable = false,
        updatable = false,
        columnDefinition = "varchar(255)"
    )
    var preferDistance: UserDesiredPartner.PreferDistance = preferDistance
        private set

    companion object {

        fun UserDesiredPartner.toJpaEntity() = UserDesiredPartnerJpaEntity(
            id = id.value,
            birthYearRangeStart = birthYearRange.start.value,
            birthYearRangeEnd = birthYearRange.endInclusive.value,
            jobOccupations = jobOccupations,
            preferDistance = preferDistance,
        )

    }

    fun toDomainEntity() = UserDesiredPartner(
        id = User.Id(id),
        birthYearRange = Year.of(birthYearRangeStart)..Year.of(birthYearRangeEnd),
        jobOccupations = jobOccupations,
        preferDistance = preferDistance,
    )

}

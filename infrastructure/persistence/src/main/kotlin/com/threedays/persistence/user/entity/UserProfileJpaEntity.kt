package com.threedays.persistence.user.entity

import com.threedays.domain.user.vo.JobOccupation
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserProfile
import com.threedays.domain.user.vo.Gender
import com.threedays.persistence.user.entity.CompanyJpaEntity.Companion.toJpaEntity
import com.threedays.persistence.user.entity.LocationJpaEntity.Companion.toJpaEntity
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Year
import java.util.*

@Entity
@Table(name = "user_profiles")
class UserProfileJpaEntity(
    id: UUID,
    gender: Gender,
    birthYear: Int,
    company: CompanyJpaEntity?,
    jobOccupation: JobOccupation,
    locations: List<LocationJpaEntity>,
) {

    @Id
    var id: UUID = id
        private set

    @Column
    var gender: String = gender.name
        private set

    @Column
    var birthYear: Int = birthYear
        private set

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company", nullable = true)
    var company: CompanyJpaEntity? = company
        private set


    @Enumerated(EnumType.STRING)
    @Column(name = "job_occupation")
    var jobOccupation: JobOccupation = jobOccupation
        private set

    @OneToMany(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_profile_locations",
        joinColumns = [JoinColumn(name = "user_profile_id")],
    )
    var locations: List<LocationJpaEntity> = locations
        private set

    companion object {

        fun UserProfile.toJpaEntity() = UserProfileJpaEntity(
            id = id.value,
            gender = gender,
            birthYear = birthYear.value,
            company = company?.toJpaEntity(),
            jobOccupation = jobOccupation,
            locations = locations.map { it.toJpaEntity() },
        )

    }

    fun toDomainEntity() = UserProfile(
        id = User.Id(id),
        gender = Gender.valueOf(gender),
        birthYear = Year.of(birthYear),
        company = company?.toDomainEntity(),
        jobOccupation = jobOccupation,
        locations = locations.map { it.toDomainEntity() },
    )

}

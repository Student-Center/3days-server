package com.threedays.persistence.user.entity

import com.threedays.domain.user.entity.Job
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "jobs")
class JobJpaEntity(
    id: UUID,
    occupation: Job.Occupation,
    name: String,
) {

    @Id
    var id: UUID = id
        private set


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, columnDefinition = "varchar(255)")
    var occupation: Job.Occupation = occupation
        private set

    @Column(nullable = false, updatable = false)
    var name: String = name
        private set

    companion object {

        fun Job.toJpaEntity() = JobJpaEntity(
            id = id.value,
            occupation = occupation,
            name = name,
        )

    }

    fun toDomainEntity() = Job(
        id = Job.Id(id),
        occupation = occupation,
        name = name,
    )

}

package com.threedays.persistence.user.entity

import com.threedays.domain.user.entity.Company
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "companies")
class CompanyJpaEntity(
    id: UUID,
    name: String,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "name", nullable = false, updatable = false)
    var name: String = name
        private set

    companion object {

        fun Company.toJpaEntity() = CompanyJpaEntity(
            id = id.value,
            name = name,
        )

    }

    fun toDomainEntity() = Company(
        id = Company.Id(id),
        name = name,
    )

}

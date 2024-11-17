package com.threedays.domain.user.entity

import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.UUIDTypeId
import java.util.*

/**
 * 회사 엔티티
 * @property id 회사 ID
 * @property name 회사명
 */
data class Company(
    override val id: Id,
    val name: String,
) : DomainEntity<Company, Company.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    val display: String
        get() = name

}

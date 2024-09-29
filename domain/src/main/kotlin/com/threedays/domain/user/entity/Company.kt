package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.CompanyId
import com.threedays.support.common.base.domain.DomainEntity

/**
 * 회사 엔티티
 * @property id 회사 ID
 * @property name 회사명
 */
data class Company(
    override val id: CompanyId,
    val name: String,
) : DomainEntity<Company, CompanyId>()

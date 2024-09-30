package com.threedays.support.common.base.domain

import com.threedays.support.common.exception.NotFoundException

interface QueryRepository<DE : DomainEntity<DE, DEID>, DEID : TypeId<*>> {

    fun find(id: DEID): DE?
    fun get(id: DEID): DE =
        find(id) ?: throw NotFoundException("${this::class.simpleName} not found by id: $id")

}

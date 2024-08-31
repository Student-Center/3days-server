package com.threedays.support.common.base.domain

interface QueryRepository<DE : DomainEntity<DE, DEID>, DEID : TypeId<*>> {

    fun find(id: DEID): DE?

}

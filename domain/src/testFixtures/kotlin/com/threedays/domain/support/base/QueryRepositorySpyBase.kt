package com.threedays.domain.support.base

import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.QueryRepository
import com.threedays.support.common.base.domain.TypeId
import java.util.concurrent.ConcurrentHashMap

abstract class QueryRepositorySpyBase<E : DomainEntity<E, ID>, ID : TypeId<*>> :
    QueryRepository<E, ID> {

    protected val storage: MutableMap<ID, E> = ConcurrentHashMap()

    override fun find(id: ID): E? = storage[id]

    open fun init() {}

    open fun clear() = storage.clear()

}

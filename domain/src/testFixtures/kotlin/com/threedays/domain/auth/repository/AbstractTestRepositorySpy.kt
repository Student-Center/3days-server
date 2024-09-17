package com.threedays.domain.auth.repository

import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.TypeId
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractTestRepositorySpy<ID : TypeId<*>, E : DomainEntity<E, ID>> {
    protected val storage: MutableMap<ID, E> = ConcurrentHashMap()

    open fun init() {}

    open fun clear() = storage.clear()

}

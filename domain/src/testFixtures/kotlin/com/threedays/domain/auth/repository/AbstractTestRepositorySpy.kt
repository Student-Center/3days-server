package com.threedays.domain.auth.repository

import java.util.concurrent.ConcurrentHashMap

abstract class AbstractTestRepositorySpy<ID : Any, E : Any> {
    protected val storage: MutableMap<ID, E> = ConcurrentHashMap()

    open fun init() {}

    open fun clear() = storage.clear()

}

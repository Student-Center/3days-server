package com.sc.weave2.support.common.base.domain

interface Repository<AR : AggregateRoot<AR, ARID>, ARID : TypeId<*>> {

    fun add(root: AR)
    fun find(id: ARID): AR?
    fun remove(id: ARID)
    fun remove(root: AR)

}

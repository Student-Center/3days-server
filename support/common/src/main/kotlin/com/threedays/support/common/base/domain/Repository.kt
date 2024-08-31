package com.threedays.support.common.base.domain

interface Repository<AR : AggregateRoot<AR, ARID>, ARID : TypeId<*>> {

    fun save(root: AR)
    fun find(id: ARID): AR?
    fun delete(id: ARID)
    fun delete(root: AR)

}

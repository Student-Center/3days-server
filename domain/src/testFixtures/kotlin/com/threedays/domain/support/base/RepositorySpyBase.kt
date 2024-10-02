package com.threedays.domain.support.base

import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.Repository
import com.threedays.support.common.base.domain.TypeId
import java.util.concurrent.ConcurrentHashMap

abstract class RepositorySpyBase<AR : AggregateRoot<AR, ARID>, ARID : TypeId<*>> :
    Repository<AR, ARID> {

    protected val entities = ConcurrentHashMap<ARID, AR>()

    override fun save(root: AR) {
        entities[root.id] = root
    }

    override fun find(id: ARID): AR? {
        return entities[id]
    }

    override fun delete(id: ARID) {
        entities.remove(id)
    }

    override fun delete(root: AR) {
        entities.remove(root.id)
    }

    fun clear() {
        entities.clear()
    }

}

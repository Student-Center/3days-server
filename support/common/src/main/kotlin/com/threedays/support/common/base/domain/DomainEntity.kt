package com.threedays.support.common.base.domain

abstract class DomainEntity<T : DomainEntity<T, ID>, ID : TypeId<*>> {
    abstract val id: ID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainEntity<*, *>

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "${javaClass.simpleName}(id=$id)"
}

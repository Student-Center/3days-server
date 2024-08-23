package com.sc.weave2.support.common.base.domain

import java.util.*

abstract class UUIDTypeId(override val value: UUID) : TypeId<UUID>(value) {
    companion object {

        inline fun <reified T : UUIDTypeId> from(value: UUID): T =
            T::class.java.getConstructor(UUID::class.java).newInstance(value)

        inline fun <reified T : UUIDTypeId> from(id: String): T = runCatching {
            from<T>(UUID.fromString(id))
        }.getOrElse {
            throw IllegalArgumentException("Invalid ${T::class.simpleName}")
        }

        inline fun <reified T : UUIDTypeId> random(): T = from<T>(UUID.randomUUID())
    }

}

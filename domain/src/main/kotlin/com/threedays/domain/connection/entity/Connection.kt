package com.threedays.domain.connection.entity

import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import java.util.UUID

data class Connection(
    override val id: Id,
): AggregateRoot<Connection, Connection.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

}

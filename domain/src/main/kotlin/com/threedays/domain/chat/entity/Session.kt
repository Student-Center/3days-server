package com.threedays.domain.chat.entity

import com.threedays.domain.chat.entity.Session.Id
import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.TypeId


data class Session(
    override val id: Id,
    val userId: User.Id,
) : DomainEntity<Session, Id>() {

    data class Id(override val value: String) : TypeId<String>(value)

    companion object {

        fun create(
            sessionId: Id,
            userId: User.Id
        ): Session {
            return Session(
                id = sessionId,
                userId = userId,
            )
        }
    }


}

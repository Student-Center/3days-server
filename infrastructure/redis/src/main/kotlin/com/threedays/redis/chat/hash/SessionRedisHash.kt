package com.threedays.redis.chat.hash

import com.threedays.domain.chat.entity.Session
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash("session")
data class SessionRedisHash(
    @Id
    val id: String,
    @Indexed
    val userId: String,
) {

    companion object {

        fun Session.toRedisHash() = SessionRedisHash(
            id = id.toString(),
            userId = userId.toString(),
        )
    }

    fun toDomain() = Session(
        id = Session.Id(id),
        userId = UUIDTypeId.from(userId),
    )
}

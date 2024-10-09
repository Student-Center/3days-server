package com.threedays.redis.auth

import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive


@RedisHash("refresh_token")
class RefreshTokenRedisHash(
    id: String,
    token: String,
    ttl: Long,
) {

    @Id
    var id: String = id
        private set

    var token: String = token
        private set

    @TimeToLive
    var ttl: Long = ttl
        private set

    companion object {

        fun of(
            refreshToken: RefreshToken,
            ttl: Long
        ): RefreshTokenRedisHash {
            return RefreshTokenRedisHash(
                id = refreshToken.userId.value.toString(),
                token = refreshToken.value,
                ttl = ttl
            )
        }

    }

    fun toDomain(): RefreshToken {
        return RefreshToken(
            userId = UUIDTypeId.from(id),
            value = token
        )
    }

}

package com.threedays.redis.auth

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.AuthCodeId
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@RedisHash("auth_code")
class AuthCodeRedisHash(
    id: UUID,
    clientOS: ClientOS,
    phoneNumber: String,
    code: AuthCode.Code,
    ttl: Long
) {

    @Id
    var id: String = id.toString()
      private set

    var clientOS: String = clientOS.name
        private set

    var phoneNumber: String = phoneNumber
        private set

    var code: AuthCode.Code = code
        private set

    @TimeToLive
    var ttl: Long = ttl
        private set

    companion object {
        fun from(authCode: AuthCode): AuthCodeRedisHash {
            return AuthCodeRedisHash(
                id = authCode.id.value,
                clientOS = authCode.clientOS,
                phoneNumber = authCode.phoneNumber.value,
                code = authCode.code,
                ttl = LocalDateTime.now().until(authCode.expireAt, ChronoUnit.SECONDS)
            )
        }
    }

    fun toDomain(): AuthCode {
        return AuthCode(
            id = AuthCodeId(UUID.fromString(id)),
            clientOS = ClientOS.valueOf(clientOS),
            phoneNumber = PhoneNumber(phoneNumber),
            code = code,
            expireAt = LocalDateTime.now().plusSeconds(ttl)
        )
    }

}

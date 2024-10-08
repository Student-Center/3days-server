package com.threedays.redis.auth

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@RedisHash("auth_code")
class AuthCodeRedisHash(
    id: String,
    clientOS: String,
    phoneNumber: String,
    code: String,
    ttl: Long
) {

    @Id
    var id: String = id
        private set

    var clientOS: String = clientOS
        private set

    var phoneNumber: String = phoneNumber
        private set

    var code: String = code
        private set

    @TimeToLive
    var ttl: Long = ttl
        private set

    companion object {

        fun from(authCode: AuthCode): AuthCodeRedisHash {
            return AuthCodeRedisHash(
                id = authCode.id.value.toString(),
                clientOS = authCode.clientOS.name,
                phoneNumber = authCode.phoneNumber.value,
                code = authCode.code.value,
                ttl = LocalDateTime.now().until(authCode.expireAt, ChronoUnit.SECONDS)
            )
        }
    }

    fun toDomain(): AuthCode {
        return AuthCode(
            id = AuthCode.Id(UUID.fromString(id)),
            clientOS = ClientOS.valueOf(clientOS),
            phoneNumber = PhoneNumber(phoneNumber),
            code = AuthCode.Code(code),
            expireAt = LocalDateTime.now().plusSeconds(ttl)
        )
    }

}

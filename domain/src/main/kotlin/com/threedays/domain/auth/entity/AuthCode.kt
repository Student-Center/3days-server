package com.threedays.domain.auth.entity

import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.vo.AuthCodeId
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import java.time.LocalDateTime

data class AuthCode(
    override val id: AuthCodeId,
    val clientOS: ClientOS,
    val phoneNumber: PhoneNumber,
    val code: Code,
    val expireAt: LocalDateTime,
) : AggregateRoot<AuthCode, AuthCodeId>() {

    @JvmInline
    value class Code(val value: String) {

        init {
            require(value.length == 6) { "인증 코드는 6자리 숫자로 구성되어야 합니다." }
            require(value.all { it.isDigit() }) { "인증 코드는 숫자로만 구성되어야 합니다." }
        }

        companion object {

            fun generate(): Code {
                val random: Int = (0..999999).random()
                val code: String = random.toString().padStart(6, '0')
                return Code(code)
            }

        }
    }

    companion object {

        fun create(
            clientOS: ClientOS,
            phoneNumber: String,
            expireAt: LocalDateTime
        ): AuthCode {
            return AuthCode(
                id = UUIDTypeId.random(),
                clientOS = clientOS,
                phoneNumber = PhoneNumber(phoneNumber),
                code = Code.generate(),
                expireAt = expireAt
            )
        }
    }

    fun verify(code: Code) {
        if (this.code != code) {
            throw AuthException.InvalidAuthCodeException()
        }

        if (LocalDateTime.now() > expireAt) {
            throw AuthException.AuthCodeExpiredException()
        }
    }

}

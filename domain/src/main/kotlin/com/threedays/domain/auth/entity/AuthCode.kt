package com.threedays.domain.auth.entity

import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import java.time.LocalDateTime
import java.util.*

data class AuthCode(
    override val id: Id,
    val clientOS: ClientOS,
    val phoneNumber: PhoneNumber,
    val code: Code,
    val expireAt: LocalDateTime,
) : AggregateRoot<AuthCode, AuthCode.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

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
            phoneNumber: PhoneNumber,
            expireAt: LocalDateTime
        ): AuthCode {
            return AuthCode(
                id = UUIDTypeId.random(),
                clientOS = clientOS,
                phoneNumber = phoneNumber,
                code = Code.generate(),
                expireAt = expireAt
            )
        }

        fun testerCode(
            id: UUID,
            phoneNumber: PhoneNumber,
            code: String
        ): AuthCode {
            return AuthCode(
                id = Id(id),
                clientOS = ClientOS.AOS,
                phoneNumber = phoneNumber,
                code = Code(code),
                expireAt = LocalDateTime.MAX
            )
        }
    }

    fun verify(code: Code): AuthCode {
        if (this.code != code) {
            throw AuthException.InvalidAuthCodeException()
        }

        if (LocalDateTime.now() > expireAt) {
            throw AuthException.AuthCodeExpiredException()
        }

        return this
    }

    fun getSmsMessage(): String = when (clientOS) {
        ClientOS.AOS -> "<#> 3days - 인증번호는 ${code.value} 입니다."
        ClientOS.IOS -> "3days - 인증번호는 ${code.value} 입니다."
    }

}

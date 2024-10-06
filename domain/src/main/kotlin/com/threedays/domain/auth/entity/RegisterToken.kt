package com.threedays.domain.auth.entity

import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.support.common.security.jwt.JwtClaims
import com.threedays.support.common.security.jwt.JwtException
import com.threedays.support.common.security.jwt.JwtTokenProvider
import java.time.Instant
import java.util.*

data class RegisterToken(
    override val value: String,
    val phoneNumber: PhoneNumber,
) : AuthToken {

    companion object {

        private const val REGISTER_TOKEN_SUBJECT = "register"
        private const val PHONE_NUMBER_CLAIM = "phoneNumber"

        fun generate(
            secret: String,
            expirationSeconds: Long,
            phoneNumber: PhoneNumber,
        ) = JwtClaims {
            registeredClaims {
                sub = REGISTER_TOKEN_SUBJECT
                exp = Instant
                    .now()
                    .plusSeconds(expirationSeconds)
                    .let { Date.from(it) }
            }
            customClaims {
                this[PHONE_NUMBER_CLAIM] = phoneNumber.value
            }
        }.let {
            JwtTokenProvider.createToken(it, secret)
        }.let {
            RegisterToken(it, phoneNumber)
        }

        fun verify(
            token: String,
            secret: String
        ): RegisterToken {
            val result: JwtClaims = JwtTokenProvider
                .verifyToken(token, secret)
                .getOrElse { exception: Throwable ->
                    when (exception) {
                        is JwtException.Expired -> throw AuthException.RegisterTokenExpiredException()
                        is JwtException -> throw AuthException.InvalidRegisterTokenException()
                        else -> throw exception
                    }
                }

            if (result.sub != REGISTER_TOKEN_SUBJECT) {
                throw AuthException.InvalidRegisterTokenException()
            }

            val phoneNumber: PhoneNumber = (result.customClaims[PHONE_NUMBER_CLAIM] as? String)
                ?.let { PhoneNumber(it) }
                ?: throw AuthException.InvalidRegisterTokenException()

            return RegisterToken(token, phoneNumber)
        }
    }

}

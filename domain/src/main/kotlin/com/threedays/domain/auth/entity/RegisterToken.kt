package com.threedays.domain.auth.entity

import com.threedays.domain.auth.exception.AuthException
import com.threedays.support.common.security.jwt.JwtClaims
import com.threedays.support.common.security.jwt.JwtException
import com.threedays.support.common.security.jwt.JwtTokenProvider
import java.time.Instant
import java.util.*

data class RegisterToken(override val value: String) : AuthToken {

    companion object {

        private const val REGISTER_TOKEN_SUBJECT = "register"

        fun generate(
            secret: String,
            expirationSeconds: Long
        ) = JwtClaims {
            registeredClaims {
                sub = REGISTER_TOKEN_SUBJECT
                exp = Instant
                    .now()
                    .plusSeconds(expirationSeconds)
                    .let { Date.from(it) }
            }
        }.let {
            JwtTokenProvider.createToken(it, secret)
        }.let {
            RegisterToken(it)
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

            return RegisterToken(token)
        }
    }

}

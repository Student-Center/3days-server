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

        // 회원 가입 토큰 만료 시간(분)
        private const val REGISTER_TOKEN_EXPIRE_MINUTES = 5L
        private const val REGISTER_TOKEN_EXPIRE_MILLIS = REGISTER_TOKEN_EXPIRE_MINUTES * 60 * 1000

        fun generate(secret: String) = JwtClaims {
            registeredClaims {
                sub = REGISTER_TOKEN_SUBJECT
                exp = Instant
                    .now()
                    .plusMillis(REGISTER_TOKEN_EXPIRE_MILLIS)
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

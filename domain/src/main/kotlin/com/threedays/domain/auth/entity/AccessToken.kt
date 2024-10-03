package com.threedays.domain.auth.entity

import com.threedays.domain.auth.exception.AuthException
import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.UUIDTypeId
import com.threedays.support.common.security.jwt.JwtClaims
import com.threedays.support.common.security.jwt.JwtException
import com.threedays.support.common.security.jwt.JwtTokenProvider
import java.time.Instant
import java.util.*

data class AccessToken(
    override val value: String,
    val userId: User.Id,
) : AuthToken {

    companion object {

        private const val ACCESS_TOKEN_SUBJECT = "access"
        private const val USER_ID_CLAIM = "userId"

        fun generate(
            secret: String,
            userId: User.Id,
            expirationSeconds: Long
        ) = JwtClaims {
            registeredClaims {
                sub = ACCESS_TOKEN_SUBJECT
                exp = Instant
                    .now()
                    .plusSeconds(expirationSeconds)
                    .let { Date.from(it) }
            }
            customClaims {
                this[USER_ID_CLAIM] = userId.value.toString()
            }
        }.let {
            JwtTokenProvider.createToken(it, secret)
        }.let {
            AccessToken(it, userId)
        }

        fun verify(
            value: String,
            secret: String,
        ): AccessToken {
            val result: JwtClaims = JwtTokenProvider
                .verifyToken(value, secret)
                .getOrElse { exception: Throwable ->
                    when (exception) {
                        is JwtException.Expired -> throw AuthException.AccessTokenExpiredException()
                        is JwtException -> throw AuthException.InvalidAccessTokenException()
                        else -> throw exception
                    }
                }

            if (result.sub != ACCESS_TOKEN_SUBJECT) {
                throw AuthException.InvalidAccessTokenException()
            }

            val userId: User.Id = (result.customClaims[USER_ID_CLAIM] as String)
                .let { UUIDTypeId.from<User.Id>(it) }

            return AccessToken(
                value,
                userId = userId,
            )
        }
    }


}

package com.sc.weave2.support.common.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException

object JwtTokenProvider {

    fun createToken(
        jwtClaims: JwtClaims,
        secret: String
    ): String =
        JWT.create().apply {
            withJWTId(jwtClaims.jti)
            withSubject(jwtClaims.sub)
            withIssuer(jwtClaims.iss)
            withAudience(*jwtClaims.aud?.toTypedArray() ?: emptyArray())
            withIssuedAt(jwtClaims.iat)
            withNotBefore(jwtClaims.nbf)
            withExpiresAt(jwtClaims.exp)
            withPayload(jwtClaims.customClaims)
        }.sign(Algorithm.HMAC256(secret))

    fun verifyToken(
        token: String,
        secret: String
    ): Result<JwtClaims> =
        verifyToken(token, Algorithm.HMAC256(secret))

    private fun verifyToken(
        token: String,
        algorithm: Algorithm
    ): Result<JwtClaims> =
        runCatching {
            JWT.require(algorithm).build().verify(token)
        }.mapCatching { claims ->
            JwtClaims.from(claims)
        }.recoverCatching { exception ->
            throw when (exception) {
                is JWTDecodeException -> JwtException.DecodeException()
                is TokenExpiredException -> JwtException.Expired()
                else -> JwtException.VerificationException()
            }
        }
}

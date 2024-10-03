package com.threedays.application.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth")
data class AuthProperties(
    val tokenSecret: String,
    val authCodeExpirationSeconds: Long,
    val registerTokenExpirationSeconds: Long,
    val accessTokenExpirationSeconds: Long,
    val refreshTokenExpirationSeconds: Long,
)

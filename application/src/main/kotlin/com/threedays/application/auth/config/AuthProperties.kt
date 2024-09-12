package com.threedays.application.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth")
data class AuthProperties(
    val authCodeExpirationSeconds: Long
)

package com.threedays.application.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.UUID

@ConfigurationProperties("auth.tester")
data class AuthTesterProperties(
    val phoneNumber: String,
    val authCode: String,
    val authCodeId: UUID,
)

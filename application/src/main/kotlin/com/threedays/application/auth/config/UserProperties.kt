package com.threedays.application.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "user")
data class UserProperties(
    val profileImage: ProfileImageProperties
) {
    data class ProfileImageProperties(
        val maxContentLength: Long,
        val uploadExpiresIn: Long
    )
}

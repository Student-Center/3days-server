package com.threedays.rest.support.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile

@Profile("dev")
@ConfigurationProperties(prefix = "discord")
@Suppress("unused")
data class DiscordProperties (
    val authSmsHookUrl: String
)

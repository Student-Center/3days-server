package com.threedays.rest.support.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "discord")
@Suppress("unused")
data class DiscordProperties(
    val authSmsHookUrl: String,
    val appEventAndMetricHookUrl: String,
)

package com.threedays.redis.support.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(value = "spring.data.redis")
data class RedisProperties(
    val host: String,
    val port: Int,
)

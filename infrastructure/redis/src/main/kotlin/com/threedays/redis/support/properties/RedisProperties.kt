package com.threedays.redis.support.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.redis")
data class RedisProperties(
    val host: String = "localhost",
    val port: Int = 6379,
    val channels: Channels = Channels()
) {
    data class Channels(
        val chat: Chat = Chat()
    ) {
        data class Chat(
            val prefix: String = "channel",
            val pattern: String = "$prefix:*"
        )
    }
}

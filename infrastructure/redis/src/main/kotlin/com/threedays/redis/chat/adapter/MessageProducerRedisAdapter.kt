package com.threedays.redis.chat.adapter

import com.threedays.application.chat.port.outbound.MessageProducer
import com.threedays.domain.chat.entity.Message
import com.threedays.redis.chat.event.MessageRedisEvent
import com.threedays.redis.support.properties.RedisProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class MessageProducerRedisAdapter(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val redisProperties: RedisProperties
) : MessageProducer {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override fun produceSendEvent(message: Message) {
        logger.debug { "Producing message sent event to redis: $message" }

        val event = MessageRedisEvent.fromDomain(message)
        val channelKey = "${redisProperties.channels.chat.prefix}:${message.channelId.value}"
        redisTemplate.convertAndSend(channelKey, event)
    }
}

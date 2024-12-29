package com.threedays.redis.chat.adapter

import com.threedays.application.chat.port.outbound.MessageProducer
import com.threedays.domain.chat.entity.Message
import com.threedays.redis.chat.event.MessageRedisEvent
import com.threedays.redis.chat.event.MessageRedisEvent.Sent.Companion.toSentEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class MessageProducerRedisAdapter(
    private val redisTemplate: RedisTemplate<String, Any>
) : MessageProducer {

    companion object {

        private val logger = KotlinLogging.logger {}
    }

    override fun produceSendEvent(message: Message) {
        logger.debug { "Producing message sent event to redis: $message" }

        val messageSentEvent: MessageRedisEvent.Sent = message.toSentEvent()
        redisTemplate.convertAndSend(messageSentEvent.getPartitionKey(), messageSentEvent)
    }

}

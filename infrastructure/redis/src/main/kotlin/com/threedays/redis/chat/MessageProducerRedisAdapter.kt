package com.threedays.redis.chat

import com.threedays.application.chat.port.outbound.MessageProducer
import com.threedays.domain.chat.entity.Message
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class MessageProducerRedisAdapter() : MessageProducer {

    companion object {

        private val logger = KotlinLogging.logger {}
    }

    override fun produce(message: Message) {
        // TODO: Implement this method
        logger.info { "Producing message: $message" }
    }

}

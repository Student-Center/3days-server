package com.threedays.redis.chat.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.threedays.application.chat.port.inbound.ReceiveMessage
import com.threedays.redis.chat.event.MessageRedisEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class MessageSubscriberRedisAdapter(
    private val receiveMessage: ReceiveMessage,
    private val objectMapper: ObjectMapper,
) : MessageListener {

    companion object {

        private val logger = KotlinLogging.logger {}
    }


    override fun onMessage(
        message: Message,
        pattern: ByteArray?
    ) {
        try {
            val channel = String(message.channel)
            val body = String(message.body)
            logger.debug { "Received message from channel '$channel': $body" }

            val event: MessageRedisEvent.Sent = objectMapper.readValue(body, MessageRedisEvent.Sent::class.java)
            handleEvent(event)
        } catch (e: Exception) {
            logger.error(e) { "Failed to process Redis message" }
        }
    }

    private fun handleEvent(event: MessageRedisEvent.Sent) {
        logger.info { "Handling received event: $event" }
        val command = ReceiveMessage.Command(event.message)
        receiveMessage.invoke(command)
    }


}

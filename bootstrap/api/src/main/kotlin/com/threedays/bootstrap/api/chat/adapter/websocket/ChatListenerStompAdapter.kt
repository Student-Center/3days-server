package com.threedays.bootstrap.api.chat.adapter.websocket

import com.threedays.application.chat.port.inbound.SendMessage
import com.threedays.bootstrap.api.chat.adapter.websocket.dto.SendUserMessageWebSocketCommand
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class ChatListenerStompAdapter(
    private val sendMessage: SendMessage
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @MessageMapping("/channel/{channelId}")
    fun onChatMessage(
        @DestinationVariable channelId: String,
        @Payload message: SendUserMessageWebSocketCommand
    ) {
        val channelId = UUID.fromString(channelId)
        logger.info { "Send message request to channel $channelId: $channelId" }

        sendMessage(message.toCommand(channelId))
    }
}

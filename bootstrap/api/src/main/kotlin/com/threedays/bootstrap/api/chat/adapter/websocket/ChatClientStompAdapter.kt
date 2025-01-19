package com.threedays.bootstrap.api.chat.adapter.websocket

import com.threedays.application.chat.port.outbound.ChatClient
import com.threedays.bootstrap.api.chat.adapter.websocket.dto.ReceiveMessageWebSocketEvent.Companion.toWebSocketEvent
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.event.ReceiveMessageEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class ChatClientStompAdapter(
    private val simpMessageTemplate: SimpMessagingTemplate,
) : ChatClient {

    companion object {

        private const val CHAT_BROKER = "/channel"
        private val logger = KotlinLogging.logger {}
    }

    override fun broadcastEvent(
        channelId: Channel.Id,
        event: ReceiveMessageEvent,
    ) {
        logger.debug { "Broadcasting message on channel ${channelId.value}: $event" }
        simpMessageTemplate.convertAndSend("${CHAT_BROKER}/${channelId.value}", event.toWebSocketEvent())
    }
}

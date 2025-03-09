package com.threedays.bootstrap.api.chat.adapter.websocket.dto

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.event.ReceiveMessageEvent
import java.time.LocalDateTime
import java.util.*

data class ReceiveMessageWebSocketEvent(
    val id: UUID,
    val channelId: UUID,
    val senderUserId: UUID,
    val content: Content,
    val createdAt: LocalDateTime,
) {

    data class Content(
        val type: String,
        val text: String,
        val cardColor: String?,
    )

    companion object {

        fun ReceiveMessageEvent.toWebSocketEvent(): ReceiveMessageWebSocketEvent {
            return ReceiveMessageWebSocketEvent(
                id = id.value,
                channelId = channelId.value,
                senderUserId = senderUserId.value,
                content = Content(
                    type = content.getTypeNameString(),
                    text = when (content) {
                        is Message.Content.Text -> (content as Message.Content.Text).text
                        is Message.Content.Card -> (content as Message.Content.Card).text
                    },
                    cardColor = when (content) {
                        is Message.Content.Card -> (content as Message.Content.Card).color.name
                        else -> null
                    }
                ),
                createdAt = createdAt,
            )
        }
    }
}

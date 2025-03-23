package com.threedays.bootstrap.api.chat.adapter.websocket.dto

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.event.ReceiveMessageEvent
import java.time.LocalDateTime
import java.util.*

data class ReceiveMessageWebSocketEvent(
    val id: UUID,
    val channelId: UUID,
    val senderUserId: UUID?,
    val content: Content,
    val createdAt: LocalDateTime,
) {

    data class Content(
        val type: String,
        val text: String,
        val title: String?,
        val cardColor: String?,
        val systemType: String?,
        val nextCardTitle: String?,
    )

    companion object {

        fun ReceiveMessageEvent.toWebSocketEvent(): ReceiveMessageWebSocketEvent {
            return ReceiveMessageWebSocketEvent(
                id = id.value,
                channelId = channelId.value,
                senderUserId = senderUserId?.value,
                content = createContent(content),
                createdAt = createdAt,
            )
        }

        private fun createContent(content: Message.Content): Content {
            return when (content) {
                is Message.Content.Text -> Content(
                    type = content.getTypeNameString(),
                    text = content.text,
                    title = null,
                    cardColor = null,
                    systemType = null,
                    nextCardTitle = null
                )
                is Message.Content.Card -> Content(
                    type = content.getTypeNameString(),
                    text = content.text,
                    title = content.title,
                    cardColor = content.color.name,
                    systemType = null,
                    nextCardTitle = null
                )
                is Message.Content.System -> Content(
                    type = content.getTypeNameString(),
                    text = content.text,
                    title = null,
                    cardColor = null,
                    systemType = content.type.name,
                    nextCardTitle = content.nextCardTitle
                )
            }
        }
    }
}

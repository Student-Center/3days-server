package com.threedays.bootstrap.api.chat.adapter.dto

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.event.ReceiveMessageEvent
import java.time.LocalDateTime
import java.util.*

data class ReceiveMessageWebSocketEvent(
    val id: UUID,
    val channelId: UUID,
    val senderUserId: UUID,
    val contentType: String,
    val contentText: String,
    val contentCardColor: String?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun ReceiveMessageEvent.toWebSocketEvent(): ReceiveMessageWebSocketEvent {
            return ReceiveMessageWebSocketEvent(
                id = id.value,
                channelId = channelId.value,
                senderUserId = senderUserId.value,
                contentType = content::class.simpleName!!,
                contentText = when (content) {
                    is Message.Content.Text -> (content as Message.Content.Text).text
                    is Message.Content.Card -> (content as Message.Content.Card).text
                },
                contentCardColor = when (content) {
                    is Message.Content.Card -> (content as Message.Content.Card).color.name
                    else -> null
                },
                createdAt = createdAt,
            )
        }
    }

}

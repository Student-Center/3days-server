package com.threedays.bootstrap.api.chat.adapter.websocket.dto

import com.threedays.application.chat.port.inbound.SendMessage
import com.threedays.application.chat.port.inbound.SendMessage.Command.Card
import com.threedays.application.chat.port.inbound.SendMessage.Command.Text
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.user.entity.User
import java.util.*

data class SendUserMessageWebSocketCommand(
    val senderUserId: String?,
    val messageContent: String,
    val messageType: MessageType,
    val cardTitle: String? = null,
) {

    enum class MessageType {
        TEXT,
        CARD,
    }

    fun toCommand(channelId: UUID): SendMessage.Command {
        return when (messageType) {
            MessageType.CARD -> Card(
                title = cardTitle ?: throw IllegalArgumentException("Card type requires title"),
                text = messageContent,
                senderUserId = User.Id(UUID.fromString(senderUserId)),
                channelId = Channel.Id(channelId)
            )

            MessageType.TEXT -> Text(
                senderUserId = User.Id(UUID.fromString(senderUserId)),
                channelId = Channel.Id(channelId),
                text = messageContent,
            )
        }
    }
}

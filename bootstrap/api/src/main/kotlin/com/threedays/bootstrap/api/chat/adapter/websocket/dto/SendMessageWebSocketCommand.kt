package com.threedays.bootstrap.api.chat.adapter.websocket.dto

import com.threedays.application.chat.port.inbound.SendMessage
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.user.entity.User
import java.util.UUID

data class SendMessageWebSocketCommand(
    val senderUserId: String,
    val messageContent: String,
    val messageType: MessageType,
) {

    enum class MessageType {
        TEXT,
        CARD,
    }

    fun toCommand(channelId: UUID): SendMessage.Command {
        return SendMessage.Command(
            senderUserId = User.Id(UUID.fromString(senderUserId)),
            channelId = Channel.Id(channelId),
            messageContent = messageContent,
            messageType = when (messageType) {
                MessageType.TEXT -> SendMessage.Command.MessageType.TEXT
                MessageType.CARD -> SendMessage.Command.MessageType.CARD
            }
        )
    }
}

package com.threedays.application.chat.port.inbound

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.user.entity.User

interface SendMessage {

    operator fun invoke(command: Command)

    data class Command(
        val senderUserId: User.Id,
        val channelId: Channel.Id,
        val messageContent: String,
        val messageType: MessageType,
    ) {
        enum class MessageType {
            TEXT,
            CARD,
        }
    }

}

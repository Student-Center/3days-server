package com.threedays.application.chat.port.inbound

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.user.entity.User

interface SendMessage {

    operator fun invoke(command: Command)

    sealed class Command(
        open val channelId: Channel.Id,
    ) {

        data class Text(
            val text: String,
            val senderUserId: User.Id,
            override val channelId: Channel.Id,
        ) : Command(channelId)

        data class Card(
            val title: String,
            val text: String,
            val senderUserId: User.Id,
            override val channelId: Channel.Id,
        ) : Command(channelId)

        data class System(
            val text: String,
            val type: Message.Content.System.Type,
            val nextCardTitle: String? = null,
            override val channelId: Channel.Id,
        ) : Command(channelId)

    }

}

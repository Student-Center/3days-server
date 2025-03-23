package com.threedays.domain.chat.event

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.user.entity.User
import java.time.LocalDateTime

data class ReceiveMessageEvent(
    val id: Message.Id,
    val channelId: Channel.Id,
    val senderUserId: User.Id?,
    val content: Message.Content,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun Message.toReceiveMessageEvent(): ReceiveMessageEvent {
            return ReceiveMessageEvent(
                id = id,
                channelId = channelId,
                senderUserId = senderUserId,
                content = content,
                createdAt = createdAt,
            )
        }
    }

}

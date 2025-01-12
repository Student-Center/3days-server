package com.threedays.redis.chat.event

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class MessageRedisEvent(
    val id: String,
    val channelId: String,
    val senderUserId: String,
    val content: Content,
    val status: Status,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    data class Content(
        val type: Type,
        val text: String,
        val cardColor: CardColor? = null
    ) {
        enum class Type {
            TEXT, CARD
        }

        enum class CardColor {
            BLUE, PINK
        }
    }

    enum class Status {
        SENT, READ
    }

    companion object {
        fun fromDomain(message: Message): MessageRedisEvent {
            val content = when (message.content) {
                is Message.Content.Text -> Content(
                    type = Content.Type.TEXT,
                    text = (message.content as Message.Content.Text).text
                )
                is Message.Content.Card -> Content(
                    type = Content.Type.CARD,
                    text = (message.content as Message.Content.Card).text,
                    cardColor = when ((message.content as Message.Content.Card).color) {
                        Message.Content.Card.Color.BLUE -> Content.CardColor.BLUE
                        Message.Content.Card.Color.PINK -> Content.CardColor.PINK
                    }
                )
            }

            return MessageRedisEvent(
                id = message.id.value.toString(),
                channelId = message.channelId.value.toString(),
                senderUserId = message.senderUserId.value.toString(),
                content = content,
                status = when (message.status) {
                    Message.Status.SENT -> Status.SENT
                    Message.Status.READ -> Status.READ
                },
                createdAt = message.createdAt,
                updatedAt = message.updatedAt
            )
        }
    }

    fun toDomain(): Message {
        val messageContent = when (content.type) {
            Content.Type.TEXT -> Message.Content.Text(content.text)
            Content.Type.CARD -> Message.Content.Card(
                text = content.text,
                color = when (content.cardColor) {
                    Content.CardColor.BLUE -> Message.Content.Card.Color.BLUE
                    Content.CardColor.PINK -> Message.Content.Card.Color.PINK
                    null -> throw IllegalStateException("Card color must not be null for CARD type")
                }
            )
        }

        return Message(
            id = Message.Id(UUID.fromString(id)),
            channelId = Channel.Id(UUID.fromString(channelId)),
            senderUserId = User.Id(UUID.fromString(senderUserId)),
            content = messageContent,
            status = when (status) {
                Status.SENT -> Message.Status.SENT
                Status.READ -> Message.Status.READ
            },
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

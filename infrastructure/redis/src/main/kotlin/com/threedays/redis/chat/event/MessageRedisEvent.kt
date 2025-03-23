package com.threedays.redis.chat.event

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.user.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class MessageRedisEvent(
    val id: String,
    val channelId: String,
    val senderUserId: String?,
    val content: Content,
    val status: Status,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    data class Content(
        val type: Type,
        val text: String,
        val title: String? = null,
        val cardColor: CardColor? = null,
        val systemType: SystemType? = null,
        val nextCardTitle: String? = null
    ) {
        enum class Type {
            TEXT, CARD, SYSTEM
        }

        enum class CardColor {
            BLUE, PINK
        }

        enum class SystemType {
            INFO, NEXT_CARD
        }
    }

    enum class Status {
        SENT, READ
    }

    companion object {
        fun fromDomain(message: Message): MessageRedisEvent {
            val content = when (val messageContent = message.content) {
                is Message.Content.Text -> Content(
                    type = Content.Type.TEXT,
                    text = messageContent.text
                )
                is Message.Content.Card -> Content(
                    type = Content.Type.CARD,
                    text = messageContent.text,
                    title = messageContent.title,
                    cardColor = when (messageContent.color) {
                        Message.Content.Card.Color.BLUE -> Content.CardColor.BLUE
                        Message.Content.Card.Color.PINK -> Content.CardColor.PINK
                    }
                )
                is Message.Content.System -> Content(
                    type = Content.Type.SYSTEM,
                    text = messageContent.text,
                    systemType = when (messageContent.type) {
                        Message.Content.System.Type.INFO -> Content.SystemType.INFO
                        Message.Content.System.Type.NEXT_CARD -> Content.SystemType.NEXT_CARD
                    },
                    nextCardTitle = messageContent.nextCardTitle
                )
            }

            return MessageRedisEvent(
                id = message.id.value.toString(),
                channelId = message.channelId.value.toString(),
                senderUserId = message.senderUserId?.value?.toString(),
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
                title = content.title ?: throw IllegalStateException("Card title must not be null for CARD type"),
                text = content.text,
                color = when (content.cardColor) {
                    Content.CardColor.BLUE -> Message.Content.Card.Color.BLUE
                    Content.CardColor.PINK -> Message.Content.Card.Color.PINK
                    null -> throw IllegalStateException("Card color must not be null for CARD type")
                }
            )
            Content.Type.SYSTEM -> Message.Content.System(
                text = content.text,
                type = when (content.systemType) {
                    Content.SystemType.INFO -> Message.Content.System.Type.INFO
                    Content.SystemType.NEXT_CARD -> Message.Content.System.Type.NEXT_CARD
                    null -> throw IllegalStateException("System type must not be null for SYSTEM type")
                },
                nextCardTitle = content.nextCardTitle
            )
        }

        return Message(
            id = Message.Id(UUID.fromString(id)),
            channelId = Channel.Id(UUID.fromString(channelId)),
            senderUserId = senderUserId?.let { User.Id(UUID.fromString(it)) },
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

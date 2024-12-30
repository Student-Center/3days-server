package com.threedays.persistence.chat.entity

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "messages")
class MessageJpaEntity(
    id: UUID,
    channelId: UUID,
    senderUserId: UUID,
    content: String,
    contentType: ContentType,
    cardColor: Message.Content.Card.Color?,
    status: Message.Status,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime?,
) {
    @Id
    var id: UUID = id
        private set

    @Column(name = "channel_id", nullable = false)
    var channelId: UUID = channelId
        private set

    @Column(name = "sender_user_id", nullable = false)
    var senderUserId: UUID = senderUserId
        private set

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String = content
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    var contentType: ContentType = contentType
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "card_color")
    var cardColor: Message.Content.Card.Color? = cardColor
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: Message.Status = status
        private set

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = createdAt
        private set

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = updatedAt
        private set

    enum class ContentType {
        TEXT, CARD
    }

    fun toDomain(): Message {
        val messageContent = when (contentType) {
            ContentType.TEXT -> Message.Content.Text(content)
            ContentType.CARD -> Message.Content.Card(
                text = content,
                color = cardColor!!
            )
        }

        return Message(
            id = Message.Id(id),
            channelId = Channel.Id(channelId),
            senderUserId = User.Id(senderUserId),
            content = messageContent,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun from(message: Message): MessageJpaEntity {
            val (contentType, content, cardColor) = when (val messageContent = message.content) {
                is Message.Content.Text -> Triple(ContentType.TEXT, messageContent.text, null)
                is Message.Content.Card -> Triple(
                    ContentType.CARD,
                    messageContent.text,
                    messageContent.color
                )
            }

            return MessageJpaEntity(
                id = message.id.value,
                channelId = message.channelId.value,
                senderUserId = message.senderUserId.value,
                content = content,
                contentType = contentType,
                cardColor = cardColor,
                status = message.status,
                createdAt = message.createdAt,
                updatedAt = message.updatedAt
            )
        }
    }
}

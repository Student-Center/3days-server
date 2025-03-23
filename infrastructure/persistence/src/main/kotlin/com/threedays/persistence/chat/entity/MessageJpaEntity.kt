package com.threedays.persistence.chat.entity

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.user.entity.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "messages")
class MessageJpaEntity(
    id: UUID,
    channelId: UUID,
    senderUserId: UUID?,
    content: String,
    contentType: ContentType,
    cardTitle: String?,
    cardColor: Message.Content.Card.Color?,
    systemMessageType: Message.Content.System.Type?,
    nextCardTitle: String?,
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

    @Column(name = "sender_user_id", nullable = true)
    var senderUserId: UUID? = senderUserId
        private set

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String = content
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    var contentType: ContentType = contentType
        private set

    @Column(name = "card_title")
    var cardTitle: String? = cardTitle
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "card_color")
    var cardColor: Message.Content.Card.Color? = cardColor
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "system_message_type")
    var systemType: Message.Content.System.Type? = systemMessageType
        private set

    @Column(name = "next_card_title")
    var nextCardTitle: String? = nextCardTitle
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
        TEXT, CARD, SYSTEM
    }

    fun toDomain(): Message {
        val messageContent = when (contentType) {
            ContentType.TEXT -> Message.Content.Text(content)
            ContentType.CARD -> Message.Content.Card(
                title = cardTitle!!,
                text = content,
                color = cardColor!!
            )

            ContentType.SYSTEM -> Message.Content.System(
                text = content,
                type = systemType!!,
                nextCardTitle = nextCardTitle
            )
        }

        return Message(
            id = Message.Id(id),
            channelId = Channel.Id(channelId),
            senderUserId = senderUserId?.let { User.Id(it) },
            content = messageContent,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {

        fun from(message: Message): MessageJpaEntity {
            return when (val messageContent = message.content) {
                is Message.Content.Text -> MessageJpaEntity(
                    id = message.id.value,
                    channelId = message.channelId.value,
                    senderUserId = message.senderUserId?.value,
                    content = messageContent.text,
                    contentType = ContentType.TEXT,
                    cardTitle = null,
                    cardColor = null,
                    systemMessageType = null,
                    nextCardTitle = null,
                    status = message.status,
                    createdAt = message.createdAt,
                    updatedAt = message.updatedAt
                )

                is Message.Content.Card -> MessageJpaEntity(
                    id = message.id.value,
                    channelId = message.channelId.value,
                    senderUserId = message.senderUserId?.value,
                    content = messageContent.text,
                    contentType = ContentType.CARD,
                    cardTitle = messageContent.title,
                    cardColor = messageContent.color,
                    systemMessageType = null,
                    nextCardTitle = null,
                    status = message.status,
                    createdAt = message.createdAt,
                    updatedAt = message.updatedAt
                )

                is Message.Content.System -> MessageJpaEntity(
                    id = message.id.value,
                    channelId = message.channelId.value,
                    senderUserId = message.senderUserId?.value,
                    content = messageContent.text,
                    contentType = ContentType.SYSTEM,
                    cardTitle = null,
                    cardColor = null,
                    systemMessageType = messageContent.type,
                    nextCardTitle = messageContent.nextCardTitle,
                    status = message.status,
                    createdAt = message.createdAt,
                    updatedAt = message.updatedAt
                )
            }
        }
    }
}

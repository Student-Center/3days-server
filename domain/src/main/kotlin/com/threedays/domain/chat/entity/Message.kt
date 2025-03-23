package com.threedays.domain.chat.entity

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.vo.Gender
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import java.time.LocalDateTime
import java.util.*

data class Message(
    override val id: Id,
    val channelId: Channel.Id,
    val senderUserId: User.Id?,
    val content: Content,
    val status: Status = Status.SENT,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) : AggregateRoot<Message, Message.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    sealed class Content {
        data class Text(val text: String) : Content()
        data class Card(
            val title: String,
            val text: String,
            val color: Color
        ) : Content() {

            enum class Color {
                BLUE, PINK,
            }
        }

        data class System(
            val text: String,
            val type: Type = Type.INFO,
            val nextCardTitle: String? = null
        ) : Content() {

            enum class Type {
                INFO,
                NEXT_CARD
            }
        }

        fun getTypeNameString(): String {
            return this::class.simpleName!!.toUpperCase()
        }
    }

    enum class Status {
        SENT,
        READ,
    }

    companion object {

        fun createCardMessage(
            channelId: Channel.Id,
            sender: User,
            title: String,
            text: String,
        ): Message {
            val color: Content.Card.Color = when (sender.profile.gender) {
                Gender.MALE -> Content.Card.Color.BLUE
                Gender.FEMALE -> Content.Card.Color.PINK
            }

            return Message(
                id = UUIDTypeId.random(),
                channelId = channelId,
                senderUserId = sender.id,
                content = Content.Card(title, text, color)
            )
        }

        fun createTextMessage(
            channelId: Channel.Id,
            sender: User,
            text: String
        ): Message {
            return Message(
                id = UUIDTypeId.random(),
                channelId = channelId,
                senderUserId = sender.id,
                content = Content.Text(text)
            )
        }

        fun createSystemMessage(
            channelId: Channel.Id,
            text: String,
            type: Content.System.Type = Content.System.Type.INFO,
        ): Message {
            return Message(
                id = UUIDTypeId.random(),
                channelId = channelId,
                senderUserId = null,
                content = Content.System(
                    text = text,
                    type = type,
                )
            )
        }

        fun createNextCardSystemMessage(
            channelId: Channel.Id,
            text: String,
            nextCardTitle: String
        ): Message {
            return Message(
                id = UUIDTypeId.random(),
                channelId = channelId,
                senderUserId = null,
                content = Content.System(
                    text = text,
                    type = Content.System.Type.NEXT_CARD,
                    nextCardTitle = nextCardTitle
                )
            )
        }
    }
}

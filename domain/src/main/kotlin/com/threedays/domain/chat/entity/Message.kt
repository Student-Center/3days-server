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
    val senderUserId: User.Id,
    val content: Content,
    val status: Status = Status.SENT,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) : AggregateRoot<Message, Message.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)

    sealed class Content {
        data class Text(val text: String) : Content()
        data class Card(
            val text: String,
            val color: Color
        ) : Content() {

            enum class Color {
                BLUE, PINK,
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
                content = Content.Card(text, color)
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
    }

}

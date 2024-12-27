package com.threedays.domain.chat.entity

import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.TypeId
import java.time.LocalDateTime
import java.util.*

data class Message(
    override val id: Id,
    val chatRoomId: Channel.Id,
    val senderId: Member.Id,
    val content: Content,
    val status: Status = Status.SENT,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val failureReason: FailureReason? = null
) : AggregateRoot<Message, Message.Id>() {

    data class Id(override val value: UUID) : TypeId<UUID>(value)

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
    }

    enum class Status {
        SENT,
        READ,
        FAILED,
    }

    enum class FailureReason {
        NETWORK_ERROR,
        INVALID_RECIPIENT,
        MESSAGE_TOO_LARGE,
        UNKNOWN_ERROR
    }

    fun markAsRead(): Message {
        require(status == Status.SENT) { "메시지가 전송된 상태에서만 읽음 처리가 가능합니다" }
        return this.copy(
            status = Status.READ,
            updatedAt = LocalDateTime.now()
        )
    }

    fun markAsFailed(reason: FailureReason): Message {
        require(status == Status.SENT) { "전송된 상태의 메시지만 실패 처리가 가능합니다" }
        return this.copy(
            status = Status.FAILED,
            updatedAt = LocalDateTime.now(),
            failureReason = reason
        )
    }
}

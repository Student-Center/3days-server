package com.threedays.domain.chat.entity

import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.TypeId
import java.time.LocalDateTime
import java.util.*

data class ChatMessage(
    override val id: Id,
    val chatRoomId: ChatRoom.Id,
    val senderId: ChatMember.Id,
    val content: String,
    var status: Status = Status.SENT,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = null,
    var failureReason: FailureReason? = null
) : AggregateRoot<ChatMessage, ChatMessage.Id>() {

    data class Id(override val value: UUID) : TypeId<UUID>(value)


    enum class Status {
        SENT,
        DELIVERED,
        READ,
        FAILED,
    }

    enum class FailureReason {
        NETWORK_ERROR,
        INVALID_RECIPIENT,
        MESSAGE_TOO_LARGE,
        UNKNOWN_ERROR
    }

    fun markAsDelivered() {
        status = Status.DELIVERED
        updatedAt = LocalDateTime.now()
    }

    fun markAsRead() {
        status = Status.READ
        updatedAt = LocalDateTime.now()
    }

    fun markAsFailed(reason: FailureReason) {
        status = Status.FAILED
        failureReason = reason
        updatedAt = LocalDateTime.now()
    }

}

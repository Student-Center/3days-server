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
    val status: Status = Status.SENT,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val failureReason: FailureReason? = null
) : AggregateRoot<ChatMessage, ChatMessage.Id>() {

    data class Id(override val value: UUID) : TypeId<UUID>(value)


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

    fun markAsRead(): ChatMessage {
        require(status == Status.SENT) { "메시지가 전송된 상태에서만 읽음 처리가 가능합니다" }
        return this.copy(
            status = Status.READ,
            updatedAt = LocalDateTime.now()
        )
    }


    fun markAsFailed(reason: FailureReason) {
        require(status == Status.SENT) { "전송된 상태의 메시지만 실패 처리가 가능합니다" }
        this.copy(
            status = Status.FAILED,
            updatedAt = LocalDateTime.now(),
            failureReason = reason
        )
    }

}

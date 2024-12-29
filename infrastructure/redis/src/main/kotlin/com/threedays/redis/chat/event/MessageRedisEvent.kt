package com.threedays.redis.chat.event

import com.threedays.domain.chat.entity.Message

sealed class MessageRedisEvent {

    data class Sent(val message: Message) {
        companion object {
            fun Message.toSentEvent() = Sent(this)
        }

        fun getPartitionKey() = message.getPartitionKey()
    }



}

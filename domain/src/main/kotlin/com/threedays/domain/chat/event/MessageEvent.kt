package com.threedays.domain.chat.event

import com.threedays.domain.chat.entity.Message

sealed class MessageEvent {

    data class Received(val message: Message) : MessageEvent()
    data class Sent(val message: Message) : MessageEvent()

}

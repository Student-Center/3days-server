package com.threedays.application.chat.port.inbound

import com.threedays.domain.chat.entity.Message

interface ReceiveMessage {

    fun invoke(command: Command)

    data class Command(
        val message: Message
    )

}

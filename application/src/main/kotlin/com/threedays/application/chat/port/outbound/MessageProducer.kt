package com.threedays.application.chat.port.outbound

import com.threedays.domain.chat.entity.Message

interface MessageProducer {

    fun produce(message: Message)

}

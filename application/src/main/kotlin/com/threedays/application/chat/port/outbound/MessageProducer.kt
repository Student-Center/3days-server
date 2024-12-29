package com.threedays.application.chat.port.outbound

import com.threedays.domain.chat.entity.Message

interface MessageProducer {

    fun produceSendEvent(message: Message)

}

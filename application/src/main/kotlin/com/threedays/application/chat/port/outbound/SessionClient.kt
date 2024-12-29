package com.threedays.application.chat.port.outbound

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.entity.Session

interface SessionClient {

    suspend fun sendMessage(
        session: Session,
        message: Message,
    )

}

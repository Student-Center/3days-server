package com.threedays.application.chat.port.outbound

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.event.ReceiveMessageEvent

interface ChatClient {

    fun broadcastEvent(
        channelId: Channel.Id,
        event: ReceiveMessageEvent
    )

}

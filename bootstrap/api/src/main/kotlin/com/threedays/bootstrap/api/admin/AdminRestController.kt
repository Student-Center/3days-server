package com.threedays.bootstrap.api.admin

import com.threedays.application.chat.port.inbound.SendMessage
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.oas.api.AdminApi
import com.threedays.oas.model.SendSystemMessageRequest
import com.threedays.oas.model.SystemMessageType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class AdminRestController(
    private val sendMessage: SendMessage,
): AdminApi {

    override fun sendSystemMessage(
        channelId: UUID,
        sendSystemMessageRequest: SendSystemMessageRequest
    ): ResponseEntity<Unit> {
        val command = SendMessage.Command.System(
            channelId = Channel.Id(channelId),
            text = sendSystemMessageRequest.text,
            type = when (sendSystemMessageRequest.type) {
                SystemMessageType.INFO -> Message.Content.System.Type.INFO
                SystemMessageType.NEXT_CARD -> Message.Content.System.Type.NEXT_CARD
            },
            nextCardTitle = sendSystemMessageRequest.nextCardTitle
        )

        sendMessage.invoke(command)
        return ResponseEntity.ok().build()
    }

}

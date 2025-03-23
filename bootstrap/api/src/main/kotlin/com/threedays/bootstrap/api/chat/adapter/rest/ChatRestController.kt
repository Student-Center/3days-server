package com.threedays.bootstrap.api.chat.adapter.rest

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.repository.MessageQueryRepository
import com.threedays.oas.api.ChatApi
import com.threedays.oas.model.GetChannelMessagesResponse
import com.threedays.oas.model.Message
import com.threedays.oas.model.MessageContent
import com.threedays.oas.model.SystemMessageType
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ChatRestController(
    private val messageQueryRepository: MessageQueryRepository
) : ChatApi {

    override fun getChannelMessages(
        channelId: UUID,
        next: UUID?,
        limit: Int
    ): ResponseEntity<GetChannelMessagesResponse> {
        val (resultMessages, resultNext) = messageQueryRepository.scrollByChannelId(
            channelId = Channel.Id(channelId),
            next = next?.let { UUIDTypeId.from(it) },
            limit = limit
        )

        return ResponseEntity.ok(createMessageResponse(resultMessages, resultNext))
    }

    private fun createMessageResponse(
        messages: List<com.threedays.domain.chat.entity.Message>,
        nextID: com.threedays.domain.chat.entity.Message.Id?,
    ): GetChannelMessagesResponse {
        val messageResponse = messages.map {
            Message(
                id = it.id.value,
                channelId = it.channelId.value,
                senderUserId = it.senderUserId?.value,
                content = createMessageContent(it.content),
                createdAt = it.createdAt.toString(),
            )
        }
        return GetChannelMessagesResponse(
            messages = messageResponse,
            next = nextID?.value
        )
    }

    private fun createMessageContent(
        content: com.threedays.domain.chat.entity.Message.Content
    ): MessageContent {
        return when (content) {
            is com.threedays.domain.chat.entity.Message.Content.Text -> MessageContent(
                type = MessageContent.Type.TEXT,
                text = content.text
            )
            is com.threedays.domain.chat.entity.Message.Content.Card -> MessageContent(
                type = MessageContent.Type.CARD,
                text = content.text,
                title = content.title,
                cardColor = MessageContent.CardColor.valueOf(content.color.name),
            )
            is com.threedays.domain.chat.entity.Message.Content.System -> MessageContent(
                type = MessageContent.Type.SYSTEM,
                text = content.text,
                systemMessageType = SystemMessageType.valueOf(content.type.name),
                nextCardTitle = content.nextCardTitle
            )
        }
    }
}

package com.threedays.bootstrap.api.chat.adapter.rest

import com.threedays.domain.chat.repository.MessageQueryRepository
import com.threedays.oas.api.ChatApi
import com.threedays.oas.model.GetChannelMessagesResponse
import com.threedays.oas.model.Message
import com.threedays.oas.model.MessageContent
import com.threedays.support.common.base.domain.UUIDTypeId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
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
            channelId = UUIDTypeId.from(channelId),
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
                senderUserId = it.senderUserId.value,

                content = MessageContent(
                    text = when (it.content) {
                        is com.threedays.domain.chat.entity.Message.Content.Text -> (it.content as com.threedays.domain.chat.entity.Message.Content.Text).text
                        is com.threedays.domain.chat.entity.Message.Content.Card -> (it.content as com.threedays.domain.chat.entity.Message.Content.Card).text
                    },
                    cardColor = when (it.content) {
                        is com.threedays.domain.chat.entity.Message.Content.Card -> (it.content as com.threedays.domain.chat.entity.Message.Content.Card).color.name
                        else -> null
                    }
                ),
                createdAt = it.createdAt.atOffset(ZoneOffset.UTC)
            )
        }
        return GetChannelMessagesResponse(
            messages = messageResponse,
            next = nextID?.value
        )
    }
}

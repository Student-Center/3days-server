package com.threedays.application.chat.service

import com.threedays.application.chat.port.inbound.ReceiveMessage
import com.threedays.application.chat.port.outbound.ChatClient
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.event.ReceiveMessageEvent.Companion.toReceiveMessageEvent
import com.threedays.domain.chat.repository.ChannelRepository
import com.threedays.domain.chat.repository.MessageRepository
import org.springframework.stereotype.Service

@Service
class ReceiveMessageService(
    private val chatClient: ChatClient,
    private val channelRepository: ChannelRepository,
    private val messageRepository: MessageRepository,
) : ReceiveMessage {

    override fun invoke(command: ReceiveMessage.Command) {
        val message = command.message
        saveMessage(message)
        broadcastMessage(message)
    }

    private fun saveMessage(message: Message) {
        messageRepository.save(message)
    }

    private fun broadcastMessage(message: Message) {
        val channel: Channel = channelRepository.findById(message.channelId) ?: return
        chatClient.broadcastEvent(channel.id, message.toReceiveMessageEvent())
    }
}

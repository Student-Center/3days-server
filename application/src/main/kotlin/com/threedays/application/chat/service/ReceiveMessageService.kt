package com.threedays.application.chat.service

import com.threedays.application.chat.port.inbound.ReceiveMessage
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.repository.MessageRepository
import org.springframework.stereotype.Service

@Service
class ReceiveMessageService(
    private val messageRepository: MessageRepository,
) : ReceiveMessage {

    override fun invoke(command: Message) {
        messageRepository.save(command)
    }


}

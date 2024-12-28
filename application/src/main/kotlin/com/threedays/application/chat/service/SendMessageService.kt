package com.threedays.application.chat.service

import com.threedays.application.chat.port.inbound.SendMessage
import com.threedays.application.chat.port.outbound.MessageProducer
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class SendMessageService(
    private val messageProducer: MessageProducer,
    private val userRepository: UserRepository,
) : SendMessage {

    override fun invoke(command: SendMessage.Command) {
        val (senderUserId: User.Id, channelId: Channel.Id, messageContent: String, messageType: SendMessage.Command.MessageType) = command
        val sender: User = userRepository.get(senderUserId)

        val message: Message = when (messageType) {
            SendMessage.Command.MessageType.TEXT ->
                Message.createTextMessage(channelId, sender, messageContent)

            SendMessage.Command.MessageType.CARD ->
                Message.createCardMessage(channelId, sender, messageContent)
        }

        messageProducer.produce(message)
    }


}

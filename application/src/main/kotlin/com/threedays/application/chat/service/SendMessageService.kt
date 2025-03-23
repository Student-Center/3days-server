package com.threedays.application.chat.service

import com.threedays.application.chat.port.inbound.SendMessage
import com.threedays.application.chat.port.outbound.MessageProducer
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


        val message: Message = when (command) {
            is SendMessage.Command.Card -> {
                val sender: User = userRepository.get(command.senderUserId)
                Message.createCardMessage(
                    sender = sender,
                    channelId = command.channelId,
                    title = command.title,
                    text = command.text
                )
            }

            is SendMessage.Command.Text -> {
                val sender: User = userRepository.get(command.senderUserId)
                Message.createTextMessage(
                    sender = sender,
                    channelId = command.channelId,
                    text = command.text,
                )
            }

            is SendMessage.Command.System -> when(command.type) {
                Message.Content.System.Type.NEXT_CARD -> Message.createNextCardSystemMessage(
                    channelId = command.channelId,
                    text = command.text,
                    nextCardTitle = command.nextCardTitle ?: throw IllegalArgumentException("Next card title is required"),
                )
                else -> Message.createSystemMessage(
                    channelId = command.channelId,
                    text = command.text,
                    type = command.type
                )
            }
        }

        messageProducer.produceSendEvent(message)
    }

}

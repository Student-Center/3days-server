package com.threedays.application.chat.service

import com.threedays.application.chat.port.inbound.ReceiveMessage
import com.threedays.application.chat.port.outbound.SessionClient
import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.entity.Session
import com.threedays.domain.chat.repository.ChannelRepository
import com.threedays.domain.chat.repository.MessageRepository
import com.threedays.domain.chat.repository.SessionRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class ReceiveMessageService(
    private val sessionClient: SessionClient,
    private val channelRepository: ChannelRepository,
    private val messageRepository: MessageRepository,
    private val sessionRepository: SessionRepository,
) : ReceiveMessage {

    companion object {

        private val logger = KotlinLogging.logger { }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logger.error(throwable) { "An error occurred while processing the message" }
    }

    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

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
        val sessions: List<Session> = channel.getMemberSessions(sessionRepository)

        sessions.forEach { session ->
            scope.launch {
                sessionClient.sendMessage(session, message)
            }
        }
    }
}

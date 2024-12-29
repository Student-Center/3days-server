package com.threedays.bootstrap.api.chat

import com.threedays.application.chat.port.outbound.SessionClient
import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.entity.Session
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class WebSocketSessionClient(
    private val webSocketSessionManager: WebSocketSessionManager,
) : SessionClient {

    companion object {

        private val logger = KotlinLogging.logger {}
    }

    override suspend fun sendMessage(
        session: Session,
        message: Message
    ) {
        val webSocketSession: WebSocketSession =
            webSocketSessionManager.findById(session.id) ?: run {
                logger.warn { "WebSocketSession not found: sessionId=${session.id}" }
                return
            }

        val textMessage = TextMessage(message.toString())
        webSocketSession.sendMessage(textMessage)
    }

}

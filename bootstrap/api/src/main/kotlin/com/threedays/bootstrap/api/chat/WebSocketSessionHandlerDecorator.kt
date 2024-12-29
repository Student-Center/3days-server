package com.threedays.bootstrap.api.chat

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.WebSocketHandlerDecorator
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory

@Component
class WebSocketSessionHandlerDecorator(
    private val webSocketSessionManager: WebSocketSessionManager,
) : WebSocketHandlerDecoratorFactory {

    companion object {

        private val logger = KotlinLogging.logger {}
    }

    override fun decorate(handler: WebSocketHandler) = object : WebSocketHandlerDecorator(handler) {
        override fun afterConnectionEstablished(session: WebSocketSession) {
            super.afterConnectionEstablished(session)
            webSocketSessionManager.addSession(session)
            logger.info { "WebSocketSession established: sessionId=${session.id}" }
        }

        override fun afterConnectionClosed(
            session: WebSocketSession,
            closeStatus: org.springframework.web.socket.CloseStatus
        ) {
            super.afterConnectionClosed(session, closeStatus)
            webSocketSessionManager.removeSession(session)
            logger.info { "WebSocketSession closed: sessionId=${session.id}" }
        }
    }


}

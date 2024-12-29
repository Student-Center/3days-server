package com.threedays.bootstrap.api.chat

import com.threedays.bootstrap.api.support.config.WebSocketProperties
import com.threedays.domain.chat.entity.Session
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketSessionManager(
    private val webSocketProperties: WebSocketProperties,
) {

    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    fun addSession(session: WebSocketSession) {
        val concurrentSession = ConcurrentWebSocketSessionDecorator(
            /* delegate = */ session,
            /* sendTimeLimit = */ webSocketProperties.session.sendTimeLimit,
            /* bufferSizeLimit = */ webSocketProperties.session.sendBufferSizeLimit
        )
        sessions[session.id] = concurrentSession
    }

    fun removeSession(session: WebSocketSession) {
        sessions.remove(session.id)
    }

    fun findById(sessionId: Session.Id): WebSocketSession? {
        return sessions[sessionId.value]
    }

}

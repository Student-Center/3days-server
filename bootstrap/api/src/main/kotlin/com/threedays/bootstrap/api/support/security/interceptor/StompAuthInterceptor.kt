package com.threedays.bootstrap.api.support.security.interceptor

import com.threedays.application.auth.config.AuthProperties
import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.chat.entity.Session
import com.threedays.domain.chat.repository.SessionRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class StompAuthInterceptor(
    private val authProperties: AuthProperties,
    private val sessionRepository: SessionRepository,
) : ChannelInterceptor {

    companion object {

        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
        private val logger = KotlinLogging.logger {}
    }

    override fun preSend(
        message: Message<*>,
        channel: MessageChannel,
    ): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)
        val command = accessor.command
        val sessionId = accessor.sessionId
            ?.let { Session.Id(it) }
            ?: return message

        when (command) {
            StompCommand.CONNECT -> handleConnect(sessionId, message)
            StompCommand.DISCONNECT -> handleDisconnect(sessionId)
            else -> return message
        }

        return message
    }

    private fun handleConnect(
        sessionId: Session.Id,
        message: Message<*>,
    ) {
        extractToken(message)?.let { token ->
            AccessToken.verify(
                value = token,
                secret = authProperties.tokenSecret,
            )
                .let { Session.create(sessionId, it.userId) }
                .also {
                    sessionRepository.save(it)
                    logger.info { "User ${it.userId} connected with session $sessionId" }
                }
        }
    }

    private fun handleDisconnect(sessionId: Session.Id) {
        sessionRepository.deleteById(sessionId)
        logger.info { "Session $sessionId disconnected" }
    }

    private fun extractToken(message: Message<*>): String? {
        val accessor = StompHeaderAccessor.wrap(message)
        val bearerToken: String? = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER)
        return if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            bearerToken.substring(BEARER_PREFIX.length)
        } else null
    }

}

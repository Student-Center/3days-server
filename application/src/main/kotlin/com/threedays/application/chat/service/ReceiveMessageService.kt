package com.threedays.application.chat.service

import com.threedays.application.chat.port.inbound.ReceiveMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class ReceiveMessageService() : ReceiveMessage {

    companion object {

        private val logger = KotlinLogging.logger { }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun invoke(command: ReceiveMessage.Command) {
        scope.launch(exceptionHandler) {
            logger.info { "Received message: $command" }
            // TODO Implement message processing
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logger.error(throwable) { "An error occurred while processing the message" }
    }

}

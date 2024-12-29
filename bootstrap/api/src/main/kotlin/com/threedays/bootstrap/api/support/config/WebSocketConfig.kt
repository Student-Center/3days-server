package com.threedays.bootstrap.api.support.config

import com.threedays.bootstrap.api.chat.WebSocketSessionHandlerDecorator
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val properties: WebSocketProperties,
    private val webSocketSessionHandlerDecorator: WebSocketSessionHandlerDecorator,
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(*properties.broker.simpleBroker.toTypedArray())
        registry.setApplicationDestinationPrefixes(*properties.broker.applicationDestinationPrefixes.toTypedArray())
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint(properties.endpoint)
            .setAllowedOrigins(*properties.allowedOrigins.toTypedArray())
    }

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
        registry.addDecoratorFactory(webSocketSessionHandlerDecorator)
    }
}

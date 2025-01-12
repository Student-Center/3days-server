package com.threedays.bootstrap.api.support.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val properties: WebSocketProperties,
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(*properties.broker.simpleBroker.toTypedArray())
        registry.setApplicationDestinationPrefixes(*properties.broker.applicationDestinationPrefixes.toTypedArray())
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint(properties.endpoint)
            .setAllowedOriginPatterns(*properties.allowedOrigins.toTypedArray())
            .withSockJS()
    }

}

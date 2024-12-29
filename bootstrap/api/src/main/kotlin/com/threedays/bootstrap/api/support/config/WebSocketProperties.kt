package com.threedays.bootstrap.api.support.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @param endpoint the endpoint to connect to
 * @param allowedOrigins list of allowed origins
 * @param sockJs whether to use SockJS
 * @param broker configuration for the broker
 * @param session configuration for the session
 */
@ConfigurationProperties(prefix = "websocket")
data class WebSocketProperties(
    val endpoint: String,
    val allowedOrigins: List<String>,
    val sockJs: Boolean,
    val broker: Broker,
    val session: Session,
) {

    /**
     * @param simpleBroker list of broker destinations
     * @param applicationDestinationPrefixes list of application destinations
     */
    data class Broker(
        val simpleBroker: List<String>,
        val applicationDestinationPrefixes: List<String>,
    )

    /**
     * @param sendTimeLimit in milliseconds
     * @param sendBufferSizeLimit in bytes
     */
    data class Session(
        val sendTimeLimit: Int,
        val sendBufferSizeLimit: Int,
    )

}

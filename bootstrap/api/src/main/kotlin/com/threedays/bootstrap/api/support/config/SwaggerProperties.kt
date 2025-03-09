package com.threedays.bootstrap.api.support.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "swagger")
data class SwaggerProperties(
    val serverConfig: ServerConfig,
) {

    data class ServerConfig(
        val dev: Server,
        val local: Server,
        val prod: Server,
    )

    data class Server(
        val url: String,
        val description: String,
    )

}

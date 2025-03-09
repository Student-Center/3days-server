package com.threedays.bootstrap.api.support.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    private val swaggerProperties: SwaggerProperties
) {

    @Bean
    fun springDocServerConfig(): OpenAPI {
        return OpenAPI()
            .addServersItem(
                Server()
                    .url(swaggerProperties.serverConfig.dev.url)
                    .description(swaggerProperties.serverConfig.dev.description)
            )
            .addServersItem(
                Server()
                    .url(swaggerProperties.serverConfig.local.url)
                    .description(swaggerProperties.serverConfig.local.description)
            )
            .addServersItem(
                Server()
                    .url(swaggerProperties.serverConfig.prod.url)
                    .description(swaggerProperties.serverConfig.prod.description)
            )
    }

}

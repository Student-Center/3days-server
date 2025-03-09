package com.threedays.bootstrap.api.support.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfig(
    private val webMvcProperties: WebMvcProperties,
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*webMvcProperties.cors.allowedOrigins.toTypedArray())
            .allowedMethods("GET", "POST", "PUT", "DELETE")
    }

}

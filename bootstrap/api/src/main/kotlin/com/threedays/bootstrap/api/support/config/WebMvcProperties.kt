package com.threedays.bootstrap.api.support.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "webmvc")
data class WebMvcProperties(
    val cors: Cors,
) {

    data class Cors(
        val allowedOrigins: List<String>,
    )

}

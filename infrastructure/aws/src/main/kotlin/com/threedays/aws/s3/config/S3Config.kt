package com.threedays.aws.s3.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config {

    @Bean
    fun s3Presigner(): S3Presigner {
        return S3Presigner.create()
    }

}

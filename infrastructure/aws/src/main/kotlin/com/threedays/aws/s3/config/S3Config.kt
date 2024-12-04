package com.threedays.aws.s3.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.regions.Region

@Configuration
class S3Config (
    @Value("\${spring.cloud.aws.region.static}")
    private val region: String
){

    @Bean
    fun s3Presigner(): S3Presigner {
        return S3Presigner.builder()
            .region(Region.of(region))
            .build()
    }

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .region(Region.of(region))
            .build()
    }

}

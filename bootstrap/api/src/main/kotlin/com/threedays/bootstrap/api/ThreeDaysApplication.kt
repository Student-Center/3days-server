package com.threedays.bootstrap.api

import com.threedays.application.support.ApplicationConfig
import com.threedays.aws.support.AwsConfig
import com.threedays.persistence.support.PersistenceConfig
import com.threedays.redis.support.config.RedisConfig
import com.threedays.rest.support.config.RestConfig
import com.threedays.sms.support.SmsConfig
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import java.util.*

@Import(
    value = [
        RestConfig::class,
        SmsConfig::class,
        PersistenceConfig::class,
        RedisConfig::class,
        AwsConfig::class,
        ApplicationConfig::class,
    ]
)
@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
class ThreeDaysApplication {

    companion object {

        const val SEOUL_TIME_ZONE = "Asia/Seoul"
    }

    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone(SEOUL_TIME_ZONE))
    }

}


fun main(args: Array<String>) {
    runApplication<ThreeDaysApplication>(*args)
}

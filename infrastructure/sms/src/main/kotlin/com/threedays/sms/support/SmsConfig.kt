package com.threedays.sms.support

import net.nurigo.sdk.NurigoApp.initialize
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

const val BASE_PACKAGE = "com.threedays.sms"

@Configuration
@ComponentScan(basePackages = [BASE_PACKAGE], lazyInit = true)
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
class SmsConfig(
    private val smsProperties: SmsProperties
) {

    @Bean
    fun coolSmsService(): DefaultMessageService {
        return initialize(
            apiKey = smsProperties.apiKey,
            apiSecretKey = smsProperties.apiSecret,
            domain = smsProperties.apiDomainUrl,
        )
    }

}

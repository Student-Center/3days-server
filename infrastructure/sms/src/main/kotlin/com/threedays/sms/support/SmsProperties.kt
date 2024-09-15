package com.threedays.sms.support

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("sms")
data class SmsProperties(
    val senderNumber: String,
    val apiKey: String,
    val apiSecret: String,
    val apiDomainUrl: String,
)

package com.threedays.rest.support.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

const val BASE_PACKAGE = "com.threedays.rest.*"

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = [BASE_PACKAGE])
@EnableFeignClients(basePackages = [BASE_PACKAGE])
@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
class RestConfig

package com.threedays.persistence.support

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

const val BASE_PACKAGE = "com.threedays.persistence"

@Configuration
@ComponentScan(basePackages = [BASE_PACKAGE], lazyInit = true)
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
class PersistenceConfig

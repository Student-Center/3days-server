package com.threedays.persistence.support

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

const val BASE_PACKAGE = "com.threedays.persistence"

@Configuration
@ComponentScan(basePackages = [BASE_PACKAGE], lazyInit = true)
@EnableConfigurationProperties
@EntityScan(basePackages = [BASE_PACKAGE])
@EnableJpaRepositories(basePackages = [BASE_PACKAGE])
@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
class PersistenceConfig

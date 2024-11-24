package com.threedays.aws.support

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

private const val BASE_PACKAGE = "com.threedays.aws"

@Configuration
@ComponentScan(basePackages = [BASE_PACKAGE])
@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
class AwsConfig

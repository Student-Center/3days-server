package com.threedays.redis.support.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.threedays.redis.chat.adapter.MessageSubscriberRedisAdapter
import com.threedays.redis.support.properties.RedisProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

const val BASE_PACKAGE = "com.threedays.redis"

@Configuration
@ComponentScan(basePackages = [BASE_PACKAGE], lazyInit = true)
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
@EnableRedisRepositories(basePackages = [BASE_PACKAGE])
class RedisConfig(
    private val redisProperties: RedisProperties,
    private val messageSubscriberRedisAdapter: MessageSubscriberRedisAdapter
) {

    @Bean
    fun connectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisProperties.host, redisProperties.port)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val serializer = GenericJackson2JsonRedisSerializer(objectMapper)

        return RedisTemplate<String, Any>().apply {
            connectionFactory = redisConnectionFactory!!
            keySerializer = StringRedisSerializer()
            valueSerializer = serializer
        }
    }

    @Bean
    fun redisMessageListenerContainer(redisConnectionFactory: RedisConnectionFactory): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(redisConnectionFactory)
        
        // 채널 패턴으로 구독
        container.addMessageListener(messageSubscriberRedisAdapter, PatternTopic(redisProperties.channels.chat.pattern))
        
        return container
    }

    private val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
        registerModule(JavaTimeModule())
    }
}

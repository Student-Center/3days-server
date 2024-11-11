package com.threedays.rest.auth.client

import com.threedays.rest.auth.dto.SendDiscordMessageRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "discord",
    url = "\${discord.auth-sms-hook-url}"
)
@Profile("dev", "local")
interface DiscordClient {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun send(@RequestBody message: SendDiscordMessageRequest)

}

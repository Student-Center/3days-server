package com.threedays.rest.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@FeignClient(
    name = "discord",
    url = "dyanamic-discord-client-url"
)
fun interface DiscordClient {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun send(
        uri: URI,
        @RequestBody message: Message,
    )

    data class Message(
        val content: String,
    )


}

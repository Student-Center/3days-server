package com.threedays.rest.user.adapter

import com.threedays.application.user.port.outbound.UserEventPort
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.vo.Gender
import com.threedays.rest.client.DiscordClient
import com.threedays.rest.support.properties.DiscordProperties
import org.springframework.stereotype.Component
import java.net.URI
import java.time.Year

@Component
class UserEventDiscordAdapter(
    private val discordProperties: DiscordProperties,
    private val discordClient: DiscordClient,
) : UserEventPort {

    private val uri by lazy {
        URI.create(discordProperties.appEventAndMetricHookUrl)
    }

    override fun issueRegisterEvent(id: User.Id, gender: Gender, birthYear: Year) {
        val displayTextForGender = when(gender) {
            Gender.MALE -> "남성"
            Gender.FEMALE -> "여성"
        }
        val content = """
            @evenryone
            ${birthYear.value}년생 $displayTextForGender 신규 유저(${id.value}) 회원가입 이벤트 발생!
        """.trimIndent()

        val request = DiscordClient.Message(content)
        discordClient.send(uri = uri, message = request)
    }

}

package com.threedays.sms.auth.adapter

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.support.common.ClientOS
import com.threedays.sms.support.SmsProperties
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.nurigo.sdk.message.model.MessageType
import net.nurigo.sdk.message.response.SingleMessageSentResponse
import net.nurigo.sdk.message.service.DefaultMessageService
import java.time.LocalDateTime

@DisplayName("[SMS][인증] CoolSmsAdapter 인증코드 SMS 발송 테스트")
class AuthCodeSmsSenderCoolSmsAdapterTest : DescribeSpec({

    val smsProperties = SmsProperties(
        senderNumber = "01012345678", // 실제 번호로 변경
        apiKey = "API",
        apiSecret = "SECRET",
        apiDomainUrl = "https://api.coolsms.co.kr"
    )
    val defaultMessageService: DefaultMessageService = mockk<DefaultMessageService>()
    val authCodeSmsSenderCoolSmsAdapter = AuthCodeSmsSenderCoolSmsAdapter(
        smsProperties = smsProperties,
        defaultMessageService = defaultMessageService
    )

    describe("인증코드 SMS 메시지 발송") {
        it("phoneNumber 에 해당하는 번호로 인증코드를 발송한다.") {
            // arrange
            val authCode = AuthCode.create(
                clientOS = ClientOS.IOS,
                phoneNumber = PhoneNumber("01012345678"),
                expireAt = LocalDateTime.now().plusMinutes(3)
            )
            every { defaultMessageService.sendOne(any()) } returns SingleMessageSentResponse(
                statusCode = "200",
                statusMessage = "OK",
                to = "01012345678", // 실제 번호로 변경
                from = "01012345678", // 실제 번호로 변경
                groupId = "GROUP_ID",
                type = MessageType.SMS,
                country = "82",
                messageId = "MESSAGE_ID",
                accountId = "ACCOUNT_ID",
            )

            // act
            authCodeSmsSenderCoolSmsAdapter.send(authCode)

            // assert
            verify(exactly = 1) { defaultMessageService.sendOne(any()) }
        }

    }


})

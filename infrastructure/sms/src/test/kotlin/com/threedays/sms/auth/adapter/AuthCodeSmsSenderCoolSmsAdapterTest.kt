package com.threedays.sms.auth.adapter

import com.threedays.domain.auth.entity.AuthCode
import com.threedays.domain.support.common.ClientOS
import io.kotest.common.ExperimentalKotest
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@OptIn(ExperimentalKotest::class)
@SpringBootTest
@DisplayName("[SMS][인증] CoolSmsAdapter 인증코드 SMS 발송 테스트")
class AuthCodeSmsSenderCoolSmsAdapterTest(
    @Autowired private val authCodeSmsSenderCoolSmsAdapter: AuthCodeSmsSenderCoolSmsAdapter,
) : DescribeSpec({

    describe("인증코드 SMS 메시지 발송").config(enabled = false) {
        it("phoneNumber 에 해당하는 번호로 인증코드를 발송한다.") {
            // arrange
            val authCode = AuthCode.create(
                clientOS = ClientOS.IOS,
                phoneNumber = "01012345678", // 실제 번호로 변경
                expireAt = LocalDateTime.now().plusMinutes(3)
            )

            // act
            authCodeSmsSenderCoolSmsAdapter.send(authCode)

            // assert 직접 확인
        }

    }


})

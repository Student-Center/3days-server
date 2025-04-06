package com.threedays.domain.user.entity

import com.threedays.domain.user.exception.UserException
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.net.URI
import java.net.URL

@DisplayName("[도메인][연결] 연결 시도")
class ConnectionAttemptTest : DescribeSpec({

    describe("프로필 이미지 생성") {
        UserProfileImage.Extension.entries.forEach { extension ->
            it("확장자가 ${extension.value}인 프로필 이미지를 생성한다") {
                // arrange
                val getProfileImageUrlAction: (UserProfileImage.Id, UserProfileImage.Extension) -> URL =
                    { _, _ -> URI("https://example.com").toURL() }

                // act
                val userProfileImage: UserProfileImage =
                    UserProfileImage.create(extension, getProfileImageUrlAction)

                // assert
                userProfileImage.extension shouldBe extension
            }
        }

        context("URL 생성작업에서 예외가 발생하는 경우") {
            it("UserException.ProfileImageUploadFailedException 예외를 던진다") {
                // arrange
                val getProfileImageUrlAction: (UserProfileImage.Id, UserProfileImage.Extension) -> URL =
                    { _, _ -> throw Exception() }

                // act & assert
                runCatching {
                    UserProfileImage.create(
                        UserProfileImage.Extension.PNG,
                        getProfileImageUrlAction
                    )
                }.onFailure { it shouldBe UserException.ProfileImageUploadFailedException() }
            }
        }
    }

})

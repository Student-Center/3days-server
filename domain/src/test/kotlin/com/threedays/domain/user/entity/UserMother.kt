package com.threedays.domain.user.entity

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.domain.auth.vo.PhoneNumber
import java.net.URL

data class UserMother(
    val fixtureMonkey: FixtureMonkey
) {
    fun createUser() : User {
        val phoneNumber = PhoneNumber("010${generateLast8PhoneNumberDigits()}")
        val userProfile = fixtureMonkey
            .giveMeBuilder<UserProfile>()
            .set(UserProfile::company, null)
            .sample()

        val userDesiredPartner = fixtureMonkey
            .giveMeBuilder<UserDesiredPartner>()
            .set(UserDesiredPartner::allowSameCompany, null)
            .sample()


        val userProfileImageUrl: URL = URL("http://example.com")
        val userProfileImage = fixtureMonkey
            .giveMeBuilder<UserProfileImage>()
            .set(UserProfileImage::url, userProfileImageUrl)
            .sampleList(1)


        return fixtureMonkey
            .giveMeBuilder<User>()
            .set(User::name, User.Name("홍길동"))
            .set(User::phoneNumber, phoneNumber)
            .set(User::profile, userProfile)
            .set(User::profileImages, userProfileImage)
            .set(User::desiredPartner, userDesiredPartner)
            .sample()
    }

    private fun generateLast8PhoneNumberDigits(): String {
        return (1000_0000..9999_9999).random().toString()
    }
}
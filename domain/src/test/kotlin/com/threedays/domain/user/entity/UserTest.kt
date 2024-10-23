package com.threedays.domain.user.entity

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobOccupation
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.Year

@DisplayName("[도메인][유저] - 유저")
class UserTest : DescribeSpec({

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()


    describe("유저 생성") {
        it("새로운 유저를 생성한다") {
            // arrange
            val name: User.Name = fixtureMonkey.giveMeBuilder<User.Name>().sample()
            val phoneNumber: PhoneNumber = PhoneNumber("01012345678")
            val userGender: Gender = fixtureMonkey.giveMeBuilder<Gender>().sample()
            val userBirthYear: Year = fixtureMonkey.giveMeBuilder<Year>().sample()
            val userCompany: Company = fixtureMonkey.giveMeBuilder<Company>().sample()
            val userJobOccupation: JobOccupation =
                fixtureMonkey.giveMeBuilder<JobOccupation>().sample()
            val userLocations: List<Location> =
                fixtureMonkey.giveMeBuilder<Location>().sampleList(3)
            val partnerBirthYearRange: BirthYearRange =
                fixtureMonkey.giveMeBuilder<BirthYearRange>().sample()
            val partnerJobOccupations: List<JobOccupation> =
                fixtureMonkey.giveMeBuilder<JobOccupation>().sampleList(10)
            val partnerPreferDistance: UserDesiredPartner.PreferDistance =
                fixtureMonkey.giveMeBuilder<UserDesiredPartner.PreferDistance>().sample()

            // act
            val user = User.create(
                name = name,
                phoneNumber = phoneNumber,
                userGender = userGender,
                userBirthYear = userBirthYear,
                userCompany = userCompany,
                userJobOccupation = userJobOccupation,
                userLocations = userLocations,
                partnerBirthYearRange = partnerBirthYearRange,
                partnerJobOccupations = partnerJobOccupations,
                partnerPreferDistance = partnerPreferDistance,
            )

            // assert
            user.id shouldNotBe null
            user.phoneNumber shouldBe phoneNumber
            user.name shouldBe name
            user.profile.gender shouldBe userGender
            user.profile.birthYear shouldBe userBirthYear
            user.profile.company shouldBe userCompany
            user.profile.jobOccupation shouldBe userJobOccupation
            user.profile.locations shouldBe userLocations
            user.desiredPartner.birthYearRange shouldBe partnerBirthYearRange
            user.desiredPartner.jobOccupations shouldBe partnerJobOccupations
            user.desiredPartner.preferDistance shouldBe partnerPreferDistance
        }
    }


})

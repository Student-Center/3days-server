package com.threedays.domain.user.entity

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobOccupation
import io.kotest.assertions.throwables.shouldThrow
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
                allowSameCompany = false,
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

        context("동일 회사 허용 여부를 선택한 경우") {
            it("회사를 선택하지 않으면 예외가 발생한다") {
                // arrange
                val name: User.Name = fixtureMonkey.giveMeBuilder<User.Name>().sample()
                val phoneNumber: PhoneNumber = PhoneNumber("01012345678")
                val userGender: Gender = fixtureMonkey.giveMeBuilder<Gender>().sample()
                val userBirthYear: Year = fixtureMonkey.giveMeBuilder<Year>().sample()
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

                val allowSameCompany: Boolean = true
                val userCompany: Company? = null

                // act & assert
                shouldThrow<IllegalArgumentException> {
                    User.create(
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
                        allowSameCompany = allowSameCompany,
                    )
                }
            }
        }

        context("동일 회사 허용 여부를 선택하지 않은 경우") {
            it("회사를 선택하면 예외가 발생한다") {
                // arrange
                val name: User.Name = fixtureMonkey.giveMeBuilder<User.Name>().sample()
                val phoneNumber: PhoneNumber = PhoneNumber("01012345678")
                val userGender: Gender = fixtureMonkey.giveMeBuilder<Gender>().sample()
                val userBirthYear: Year = fixtureMonkey.giveMeBuilder<Year>().sample()
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

                val allowSameCompany: Boolean = true
                val userCompany: Company? = null

                // act & assert
                shouldThrow<IllegalArgumentException> {
                    User.create(
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
                        allowSameCompany = allowSameCompany,
                    )
                }
            }
        }

        context("이름이 공백일 경우") {
            it("예외가 발생한다") {
                // arrange
                val name = ""

                // act & assert
                shouldThrow<IllegalArgumentException> {
                    User.Name(name)
                }
            }
        }
    }

    describe("putProfileWidget - 프로필 위젯 추가") {
        it("프로필 위젯을 추가한다") {
            // arrange
            val profileWidget: ProfileWidget = fixtureMonkey
                .giveMeBuilder<ProfileWidget>()
                .sample()

            val user: User = fixtureMonkey
                .giveMeBuilder<User>()
                .set(User::phoneNumber, PhoneNumber("01012345678"))
                .sample()

            // act
            val result: User = user.putProfileWidget(profileWidget)

            // assert
            result.profile.profileWidgets.find { it == profileWidget } shouldBe profileWidget
        }

        context("동일한 타입의 위젯이 이미 있는 경우") {
            it("내용을 수정한다") {
                // arrange
                val type = ProfileWidget.Type.BODY_TYPE
                val profileWidget: ProfileWidget = fixtureMonkey
                    .giveMeBuilder<ProfileWidget>()
                    .set(ProfileWidget::type, type)
                    .sample()

                val user: User = fixtureMonkey
                    .giveMeBuilder<User>()
                    .set(User::phoneNumber, PhoneNumber("01012345678"))
                    .sample()
                    .putProfileWidget(profileWidget)

                val updatedProfileWidget: ProfileWidget = fixtureMonkey
                    .giveMeBuilder<ProfileWidget>()
                    .set(ProfileWidget::type, type)
                    .sample()

                // act
                val result: User = user.putProfileWidget(updatedProfileWidget)

                // assert
                result.profile.profileWidgets.find { it == updatedProfileWidget } shouldBe updatedProfileWidget
            }
        }
    }

})

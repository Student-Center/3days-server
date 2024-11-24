package com.threedays.domain.user.entity

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.reposiotry.CompanyQueryRepositorySpy
import com.threedays.domain.user.reposiotry.LocationQueryRepositorySpy
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.domain.user.vo.BirthYearRange
import com.threedays.domain.user.vo.Gender
import com.threedays.domain.user.vo.JobOccupation
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.Year

@DisplayName("[도메인][유저] 유저")
class UserTest : DescribeSpec({

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()

    val locationQueryRepository = LocationQueryRepositorySpy()
    val companyQueryRepository = CompanyQueryRepositorySpy()

    afterTest {
        locationQueryRepository.clear()
        companyQueryRepository.clear()
    }

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
                        name = User.Name("홍길동"),
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

            val userDesiredPartner: UserDesiredPartner = fixtureMonkey
                .giveMeBuilder<UserDesiredPartner>()
                .set(UserDesiredPartner::allowSameCompany, null)
                .sample()

            val user: User = fixtureMonkey
                .giveMeBuilder<User>()
                .set(User::name, User.Name("홍길동"))
                .set(User::phoneNumber, PhoneNumber("01012345678"))
                .set(User::desiredPartner, userDesiredPartner)
                .sample()

            // act
            val result: User = user.putProfileWidget(profileWidget)

            // assert
            result.profile.profileWidgets.find { it == profileWidget } shouldBe profileWidget
        }

        ProfileWidget.Type.entries.forEach { widgetType ->
        context("동일한 타입($widgetType)의 위젯이 이미 있는 경우") {
                it("$widgetType 타입의 위젯 내용을 수정한다") {
                    // arrange
                    val profileWidget: ProfileWidget = fixtureMonkey
                        .giveMeBuilder<ProfileWidget>()
                        .set(ProfileWidget::type, widgetType)
                        .sample()

                    val userDesiredPartner: UserDesiredPartner = fixtureMonkey
                        .giveMeBuilder<UserDesiredPartner>()
                        .set(UserDesiredPartner::allowSameCompany, null)
                        .sample()

                    val user: User = fixtureMonkey
                        .giveMeBuilder<User>()
                        .set(User::name, User.Name("홍길동"))
                        .set(User::phoneNumber, PhoneNumber("01012345678"))
                        .set(User::desiredPartner, userDesiredPartner)
                        .sample()
                        .putProfileWidget(profileWidget)

                    val updatedProfileWidget: ProfileWidget = fixtureMonkey
                        .giveMeBuilder<ProfileWidget>()
                        .set(ProfileWidget::type, widgetType)
                        .sample()

                    // act
                    val result: User = user.putProfileWidget(updatedProfileWidget)

                    // assert
                    result.profile.profileWidgets.find { it == updatedProfileWidget } shouldBe updatedProfileWidget
                }
            }
        }
    }

    describe("updateUserInfo - 유저 정보 수정") {
        it("유저 정보를 수정한다") {
            // arrange
            val userDesiredPartner: UserDesiredPartner = fixtureMonkey
                .giveMeBuilder<UserDesiredPartner>()
                .set(UserDesiredPartner::allowSameCompany, null)
                .sample()

            val user: User = fixtureMonkey
                .giveMeBuilder<User>()
                .set(User::name, User.Name("홍길동"))
                .set(User::phoneNumber, PhoneNumber("01012345678"))
                .set(User::desiredPartner, userDesiredPartner)
                .sample()

            val name: User.Name = fixtureMonkey.giveMeBuilder<User.Name>().sample()
            val jobOccupation: JobOccupation = fixtureMonkey.giveMeBuilder<JobOccupation>().sample()
            val locations = fixtureMonkey.giveMeBuilder<Location>().sampleList(3)
            val company = fixtureMonkey.giveMeBuilder<Company>().sample()
            val allowSameCompany: Boolean = fixtureMonkey.giveMeBuilder<Boolean>().sample()

            companyQueryRepository.save(company)
            locations.forEach { locationQueryRepository.save(it) }

            // act
            val result: User = user.updateUserInfo(
                name = name,
                jobOccupation = jobOccupation,
                locationIds = locations.map { it.id },
                locationQueryRepository = locationQueryRepository,
                companyId = company.id,
                companyQueryRepository = companyQueryRepository,
                allowSameCompany = allowSameCompany,
            )

            // assert
            result.name shouldBe name
            result.profile.jobOccupation shouldBe jobOccupation
            result.profile.locations shouldBe locations
            result.profile.company shouldBe company
        }
    }

})

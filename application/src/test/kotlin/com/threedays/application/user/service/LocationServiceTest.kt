package com.threedays.application.user.service

import com.threedays.domain.auth.repository.LocationQueryRepositorySpy
import com.threedays.domain.user.entity.Location
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("[서비스] - 지역")
class LocationServiceTest : DescribeSpec({

    val locationQueryRepository = LocationQueryRepositorySpy()
    val sut = LocationService(
        locationQueryRepository = locationQueryRepository,
    )

    beforeEach {
        locationQueryRepository.init()
    }

    afterEach {
        locationQueryRepository.clear()
    }

    describe("지역 전체 조회") {
        it("지역정보를 전체 조회 한다.") {
            // arrange

            // act
            val actual: List<Location> = sut.findAll()

            // assert
            actual.isNotEmpty() shouldBe true
        }
    }

})

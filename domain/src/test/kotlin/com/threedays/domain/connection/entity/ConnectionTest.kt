package com.threedays.domain.connection.entity

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserMother
import com.threedays.support.common.base.domain.UUIDTypeId
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.time.LocalDateTime
import java.util.UUID

@DisplayName("[도메인][연결] - 연결")
class ConnectionTest : DescribeSpec({

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()
    val userMother = UserMother(fixtureMonkey)

    describe("Connection 생성") {
        it("두 유저를 매칭하여 Connection을 생성한다") {
            // arrange
            val user1 = userMother.createUser()
            val user2 = userMother.createUser()
            val beforeConnectAt = LocalDateTime.now()

            // act
            val connection = Connection.match(user1, user2)

            // assert
            connection.id shouldNotBe null
            connection.connectedAt shouldBeGreaterThan  beforeConnectAt
            connection.cancellation shouldBe null
            connection.isParticipant(user1.id) shouldBe true
            connection.isParticipant(user2.id) shouldBe true
        }
    }

    describe("cancel") {
        it("Connection을 취소하고 취소 정보를 설정한다") {
            // arrange
            val user = userMother.createUser()
            val connection = Connection.match(user, userMother.createUser())
            val reason = ConnectionCancellation.Reason.ETC
            val detail = "개인적인 사유로 취소합니다."

            // act
            val canceledConnection = connection.cancel(user.id, reason, detail)

            // assert
            canceledConnection.cancellation shouldNotBe null
            canceledConnection.cancellation?.userId shouldBe user.id
            canceledConnection.cancellation?.reason shouldBe reason
            canceledConnection.cancellation?.detail shouldBe detail
        }

        it("취소 상세 정보가 null일 때도 Connection을 취소한다") {
            // arrange
            val user = userMother.createUser()
            val connection = Connection.match(user, userMother.createUser())
            val reason = ConnectionCancellation.Reason.ETC

            // act
            val canceledConnection = connection.cancel(user.id, reason)

            // assert
            canceledConnection.cancellation shouldNotBe null
            canceledConnection.cancellation?.userId shouldBe user.id
            canceledConnection.cancellation?.reason shouldBe reason
            canceledConnection.cancellation?.detail shouldBe null
        }
    }

    describe("isParticipant") {
        it("userId가 participant1의 id와 같으면 true를 반환한다") {
            // arrange
            val connection = Connection.match(userMother.createUser(), userMother.createUser())

            // act & assert
            connection.isParticipant(connection.participant1.id) shouldBe true
        }

        it("userId가 participant2의 id와 같으면 true를 반환한다") {
            // arrange
            val connection = Connection.match(userMother.createUser(), userMother.createUser())

            // act & assert
            connection.isParticipant(connection.participant2.id) shouldBe true
        }

        it("userId가 participant1, participant2의 id와 모두 다르면 false를 반환한다") {
            // arrange
            val connection = Connection.match(userMother.createUser(), userMother.createUser())
            val otherUserId = User.Id(UUID.randomUUID())

            // act & assert
            connection.isParticipant(otherUserId) shouldBe false
        }
    }

    describe("Id") {
        it("UUIDTypeId를 상속받는다") {
            val id = Connection.Id(UUID.randomUUID())
            id.shouldBeInstanceOf<UUIDTypeId>()
        }
    }
})
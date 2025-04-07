package com.threedays.domain.connection.entity

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.introspector.PrimaryConstructorArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.set
import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.UUIDTypeId
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.util.UUID

@DisplayName("[도메인][연결] - 연결 시도")
class ConnectionAttemptTest : DescribeSpec({

    val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(PrimaryConstructorArbitraryIntrospector.INSTANCE)
        .build()

    describe("연결 시도 생성") {
        it("새로운 연결 시도를 생성한다") {
            // arrange
            val userId = User.Id(UUID.randomUUID())

            // act
            val connectionAttempt = ConnectionAttempt.create(userId)

            // assert
            connectionAttempt.id shouldNotBe null
            connectionAttempt.userId shouldBe userId
            connectionAttempt.status shouldBe ConnectionAttempt.Status.CONNECTING
            connectionAttempt.connection shouldBe null
        }
    }

    describe("연결 성공") {
        it("연결 성공 상태로 변경한다") {
            // arrange
            val connectionAttempt = fixtureMonkey.giveMeBuilder<ConnectionAttempt>()
                .set(ConnectionAttempt::status, ConnectionAttempt.Status.CONNECTING)
                .sample()
            val connection = fixtureMonkey.giveMeOne<Connection>()

            // act
            val connectedAttempt = connectionAttempt.connect(connection)

            // assert
            connectedAttempt.status shouldBe ConnectionAttempt.Status.CONNECTED
            connectedAttempt.connection shouldBe connection
        }
    }

    describe("연결 실패") {
        it("연결 실패 상태로 변경한다") {
            // arrange
            val connectionAttempt = fixtureMonkey.giveMeBuilder<ConnectionAttempt>()
                .set(ConnectionAttempt::status, ConnectionAttempt.Status.CONNECTING)
                .sample()

            // act
            val failedAttempt = connectionAttempt.fail()

            // assert
            failedAttempt.status shouldBe ConnectionAttempt.Status.FAILED
            failedAttempt.connection shouldBe connectionAttempt.connection
        }
    }

    describe("Status.from") {
        it("문자열로부터 Status 객체를 생성한다") {
            ConnectionAttempt.Status.entries.forEach { status ->
                ConnectionAttempt.Status.from(status.toString()) shouldBe status
            }
        }

        it("알 수 없는 문자열로부터 UNKNOWN Status 객체를 생성한다") {
            ConnectionAttempt.Status.from("INVALID_STATUS") shouldBe ConnectionAttempt.Status.UNKNOWN
        }
    }

    describe("Id") {
        it("UUIDTypeId를 상속받는다") {
            val id = ConnectionAttempt.Id(UUID.randomUUID())
            id.shouldBeInstanceOf<UUIDTypeId>()
        }
    }
})
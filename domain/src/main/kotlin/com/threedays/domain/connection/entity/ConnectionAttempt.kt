package com.threedays.domain.connection.entity

import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.AggregateRoot
import com.threedays.support.common.base.domain.UUIDTypeId
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class ConnectionAttempt(
    override val id: Id,
    val connection: Connection?,
    val userId: User.Id,
    val status: Status,
    val attemptDate: LocalDate,
    val createdAt: LocalDateTime,
) : AggregateRoot<ConnectionAttempt, ConnectionAttempt.Id>() {

    fun connect(connection: Connection): ConnectionAttempt {
        return copy(
            status = Status.CONNECTED,
            connection = connection
        )
    }

    fun fail(): ConnectionAttempt {
        return copy(
            status = Status.FAILED
        )
    }

    data class Id(override val value: UUID) : UUIDTypeId(value)
    enum class Status {
        CONNECTING, CONNECTED, FAILED, UNKNOWN,
        ;

        companion object {
            private val logger = KotlinLogging.logger { }

            fun from(status: String): Status {
                return when (status) {
                    "CONNECTING" -> CONNECTING
                    "CONNECTED" -> CONNECTED
                    "FAILED" -> FAILED
                    else -> {
                        logger.warn { "ConnectionAttempt status $status not found" }
                        UNKNOWN
                    }
                }
            }
        }
    }

    companion object {
        fun create(userId: User.Id): ConnectionAttempt {
            val now = LocalDateTime.now()
            return ConnectionAttempt(
                id = UUIDTypeId.random(),
                userId = userId,
                status = Status.CONNECTING,
                attemptDate = now.toLocalDate(),
                createdAt = now,
                connection = null,
            )
        }
    }
}

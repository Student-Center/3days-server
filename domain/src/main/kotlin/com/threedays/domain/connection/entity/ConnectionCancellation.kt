package com.threedays.domain.connection.entity

import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.DomainEntity
import com.threedays.support.common.base.domain.UUIDTypeId
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

data class ConnectionCancellation(
    override val id: Id,
    val userId: User.Id,
    val reason: Reason,
    val detail: String?,
    val createdAt: LocalDateTime,
) : DomainEntity<ConnectionCancellation, ConnectionCancellation.Id>() {

    data class Id(override val value: UUID) : UUIDTypeId(value)
    enum class Reason {
        NO_RESPONSE, FIRST_MESSAGE_TIMEOUT, DUPLICATED, REPORTED, ADMIN_CANCELLATION, ETC, UNKNOWN,
        ;

        companion object {
            private val logger = KotlinLogging.logger { }

            fun from(value: String): Reason {
                return when (value) {
                    "NO_RESPONSE" -> NO_RESPONSE
                    "FIRST_MESSAGE_TIMEOUT" -> FIRST_MESSAGE_TIMEOUT
                    "DUPLICATED" -> DUPLICATED
                    "REPORTED" -> REPORTED
                    "ADMIN_CANCELLATION" -> ADMIN_CANCELLATION
                    "ETC" -> ETC
                    else -> {
                        logger.warn { "ConnectionCancellation reason $value not found" }
                        UNKNOWN
                    }
                }
            }
        }
    }

    companion object {
        fun create(
            userId: User.Id,
            reason: Reason,
            detail: String? = null,
        ): ConnectionCancellation {
            return ConnectionCancellation(
                id = UUIDTypeId.random(),
                userId = userId,
                reason = reason,
                detail = detail,
                createdAt = LocalDateTime.now(),
            )
        }
    }
}

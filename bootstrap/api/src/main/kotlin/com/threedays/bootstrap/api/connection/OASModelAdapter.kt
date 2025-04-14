package com.threedays.bootstrap.api.connection

import com.threedays.bootstrap.api.user.OASModelAdapter as UserOASModelAdapter
import com.threedays.domain.user.entity.User
import com.threedays.oas.model.Connection
import com.threedays.oas.model.ConnectionAttempt
import com.threedays.oas.model.ConnectionAttemptStatus
import com.threedays.oas.model.ConnectionCancellation
import com.threedays.oas.model.ConnectionCancellationReason
import com.threedays.oas.model.Participant

object OASModelAdapter {
    fun toOASModel(userId: User.Id, domainModel: com.threedays.domain.connection.entity.ConnectionAttempt): ConnectionAttempt = ConnectionAttempt(
        id = domainModel.id.value,
        userId = domainModel.userId.value,
        connection = domainModel.connection?.let { toOASModel(userId, it) },
        status = ConnectionAttemptStatus.valueOf(domainModel.status.name),
        attemptDate = domainModel.attemptDate.toString(),
        createdAt = domainModel.createdAt.toString(),
    )

    private fun toOASModel(userId: User.Id, domainModel: com.threedays.domain.connection.entity.Connection): Connection = Connection(
        id = domainModel.id.value,
        partner = if (domainModel.participant1.id == userId) toOASModel(domainModel.participant2) else toOASModel(domainModel.participant1),
        connectedAt = domainModel.connectedAt.toString(),
        cancellation = domainModel.cancellation?.let { toConnectionCancellation(it) },
    )

    private fun toConnectionCancellation(domainModel: com.threedays.domain.connection.entity.ConnectionCancellation): ConnectionCancellation =
        ConnectionCancellation(
            id = domainModel.id.value,
            userId = domainModel.userId.value,
            reason = ConnectionCancellationReason.valueOf(domainModel.reason.name),
            createdAt = domainModel.createdAt.toString(),
            detail = domainModel.detail,
        )

    private fun toOASModel(domainModel: com.threedays.domain.connection.entity.Participant) = Participant(
        userId = domainModel.id.value,
        name = domainModel.name.value,
        phoneNumber = domainModel.phoneNumber.value,
        profile = UserOASModelAdapter.toUserProfileDisplayInfo(domainModel.profile),
        profileWidgets = domainModel.profile.profileWidgets.map { UserOASModelAdapter.toOASModel(it) },
        profileImages = domainModel.profileImages.map { UserOASModelAdapter.toOASModel(it) },
    )
}

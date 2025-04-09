package com.threedays.domain.connection.repository

import com.threedays.domain.connection.entity.ConnectionAttempt
import com.threedays.domain.support.base.RepositorySpyBase
import com.threedays.domain.user.entity.User


class ConnectionAttemptRepositorySpy : ConnectionAttemptRepository, RepositorySpyBase<ConnectionAttempt, ConnectionAttempt.Id>() {
    override fun findLatestConnectionAttempt(userId: User.Id): ConnectionAttempt? {
        return entities.values
            .filter { it.userId == userId }
            .maxByOrNull { it.createdAt }
    }
}

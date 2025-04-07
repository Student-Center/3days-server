package com.threedays.domain.connection.repository

import com.threedays.domain.connection.entity.ConnectionAttempt
import com.threedays.domain.support.base.RepositorySpyBase


class ConnectionAttemptRepositorySpy : ConnectionAttemptRepository, RepositorySpyBase<ConnectionAttempt, ConnectionAttempt.Id>() {
}

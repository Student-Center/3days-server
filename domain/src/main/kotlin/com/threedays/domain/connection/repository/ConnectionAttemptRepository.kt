package com.threedays.domain.connection.repository

import com.threedays.domain.connection.entity.ConnectionAttempt
import com.threedays.support.common.base.domain.Repository

interface ConnectionAttemptRepository : Repository<ConnectionAttempt, ConnectionAttempt.Id> {
}

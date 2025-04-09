package com.threedays.application.connection.service

import com.threedays.application.connection.port.inbound.GetCurrentConnectionAttempt
import com.threedays.domain.connection.repository.ConnectionAttemptRepository
import com.threedays.support.common.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class ConnectionQueryService(
    val connectionAttemptRepository: ConnectionAttemptRepository,
) : GetCurrentConnectionAttempt {

    override fun invoke(command: GetCurrentConnectionAttempt.Command): GetCurrentConnectionAttempt.Result {
        val connectionAttempt = connectionAttemptRepository.findLatestConnectionAttempt(command.userId)
            ?: throw NotFoundException("connection attempt not found")
        return GetCurrentConnectionAttempt.Result(connectionAttempt)
    }

}

package com.threedays.application.user.service

import com.threedays.application.user.port.inbound.UpdateDesiredPartner
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDesiredPartnerService(
    private val userRepository: UserRepository,
) : UpdateDesiredPartner {

    @Transactional
    override fun invoke(command: UpdateDesiredPartner.Command): User {
        return userRepository
            .get(command.userId)
            .updateDesiredPartner(
                birthYearRange = command.birthYearRange,
                jobOccupations = command.jobOccupations,
                preferDistance = command.preferDistance,
            )
            .also { userRepository.save(it) }
    }
}

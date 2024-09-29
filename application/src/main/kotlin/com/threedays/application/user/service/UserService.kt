package com.threedays.application.user.service

import com.threedays.application.user.port.inbound.CommandUser
import com.threedays.application.user.vo.command.UserCommand
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
): CommandUser {

    override fun register(command: UserCommand.Register): User {
        return User.create(
            name = command.name,
            userGender = command.userGender,
            userBirthYear = command.userBirthYear,
            userLocationIds = command.userLocationIds,
            userCompanyId = command.userCompanyId,
            userJobId = command.userJobId,
            partnerJobOccupations = command.partnerJobOccupations,
            partnerBirthYearRange = command.partnerBirthYearRange,
            partnerPreferDistance = command.partnerPreferDistance,

        ).also {
            userRepository.save(it)
        }
    }

}

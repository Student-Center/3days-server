package com.threedays.application.user.service

import com.threedays.application.user.port.inbound.CommandUser
import com.threedays.application.user.vo.command.UserCommand
import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.entity.Job
import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.CompanyQueryRepository
import com.threedays.domain.user.repository.JobQueryRepository
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val locationQueryRepository: LocationQueryRepository,
    private val jobQueryRepository: JobQueryRepository,
    private val companyQueryRepository: CompanyQueryRepository,
) : CommandUser {

    @Transactional
    override fun register(command: UserCommand.Register): User {
        val userCompany: Company = companyQueryRepository.get(command.userCompanyId)
        val userJob: Job = jobQueryRepository.get(command.userJobId)
        val userLocations: List<Location> =
            command.userLocationIds.map { locationQueryRepository.get(it) }

        return User.create(
            name = command.name,
            phoneNumber = command.phoneNumber,
            userGender = command.userGender,
            userBirthYear = command.userBirthYear,
            userLocations = userLocations,
            userCompany = userCompany,
            userJob = userJob,
            partnerJobOccupations = command.partnerJobOccupations,
            partnerBirthYearRange = command.partnerBirthYearRange,
            partnerPreferDistance = command.partnerPreferDistance,
        ).also {
            userRepository.save(it)
        }
    }

}

package com.threedays.application.user.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.ClearTokens
import com.threedays.application.auth.port.inbound.IssueLoginTokens
import com.threedays.application.user.port.inbound.DeleteMyUser
import com.threedays.application.user.port.inbound.RegisterUser
import com.threedays.application.user.port.inbound.UpdateConnectionStatus
import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.CompanyQueryRepository
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val locationQueryRepository: LocationQueryRepository,
    private val companyQueryRepository: CompanyQueryRepository,
    private val issueLoginTokens: IssueLoginTokens,
    private val clearTokens: ClearTokens,
    private val authProperties: AuthProperties,
) : RegisterUser, UpdateConnectionStatus, DeleteMyUser {

    @Transactional
    override fun invoke(command: RegisterUser.Command): RegisterUser.Result {
        val userCompany: Company? = command.userCompanyId?.let { companyQueryRepository.get(it) }
        val userLocations: List<Location> =
            command.userLocationIds.map { locationQueryRepository.get(it) }

        val user: User = User.create(
            name = command.name,
            phoneNumber = command.phoneNumber,
            userGender = command.userGender,
            userBirthYear = command.userBirthYear,
            userLocations = userLocations,
            userCompany = userCompany,
            userJobOccupation = command.userJobOccupation,
            partnerJobOccupations = command.partnerJobOccupations,
            partnerBirthYearRange = command.partnerBirthYearRange,
            partnerPreferDistance = command.partnerPreferDistance,
            allowSameCompany = command.allowSameCompany,
        ).also {
            userRepository.save(it)
        }

        val result: IssueLoginTokens.Result = IssueLoginTokens.Command(user)
            .let { issueLoginTokens.invoke(it) }

        return RegisterUser.Result(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            expiresIn = authProperties.accessTokenExpirationSeconds,
        )
    }

    override fun invoke(command: UpdateConnectionStatus.Command): User {
        val user: User = userRepository.get(command.userId)
        return user
            .updateConnectionStatus(command.status)
            .also { userRepository.save(it) }
    }

    @Transactional
    override fun invoke(command: DeleteMyUser.Command) {
        // TODO(connection): 현재 connecting 여부를 체크하고 있는 경우 에러를 반환해야함.
        userRepository.delete(command.userId)
        clearTokens.invoke(ClearTokens.Command(command.userId))
    }

}

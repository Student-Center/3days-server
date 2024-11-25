package com.threedays.application.user.service

import com.threedays.application.auth.config.AuthProperties
import com.threedays.application.auth.port.inbound.IssueLoginTokens
import com.threedays.application.user.port.inbound.DeleteProfileWidget
import com.threedays.application.user.port.inbound.PutProfileWidget
import com.threedays.application.user.port.inbound.RegisterUser
import com.threedays.application.user.port.inbound.UpdateDesiredPartner
import com.threedays.application.user.port.inbound.UpdateUserInfo
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
    private val authProperties: AuthProperties,
) : RegisterUser,
    PutProfileWidget,
    DeleteProfileWidget,
    UpdateUserInfo,
    UpdateDesiredPartner {

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

    @Transactional
    override fun invoke(command: PutProfileWidget.Command) {
        userRepository
            .get(command.userId)
            .putProfileWidget(command.profileWidget)
            .also { userRepository.save(it) }
    }

    @Transactional
    override fun invoke(command: DeleteProfileWidget.Command) {
        userRepository
            .get(command.userId)
            .deleteProfileWidget(command.type)
            .also { userRepository.save(it) }
    }

    @Transactional
    override fun invoke(command: UpdateUserInfo.Command): User {
        return userRepository
            .get(command.userId)
            .updateUserInfo(
                name = command.name,
                jobOccupation = command.jobOccupation,
                locationIds = command.locationIds,
                locationQueryRepository = locationQueryRepository,
                companyId = command.companyId,
                companyQueryRepository = companyQueryRepository,
                allowSameCompany = command.allowSameCompany,
            )
            .also { userRepository.save(it) }
    }

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

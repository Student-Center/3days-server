package com.threedays.application.user.service

import com.threedays.application.user.port.inbound.UpdateUserInfo
import com.threedays.application.user.port.inbound.PutProfileWidget
import com.threedays.application.user.port.inbound.DeleteProfileWidget
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.repository.CompanyQueryRepository
import com.threedays.domain.user.repository.LocationQueryRepository
import com.threedays.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProfileService(
    private val userRepository: UserRepository,
    private val locationQueryRepository: LocationQueryRepository,
    private val companyQueryRepository: CompanyQueryRepository,
) : UpdateUserInfo, PutProfileWidget, DeleteProfileWidget {

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

}

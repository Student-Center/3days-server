package com.threedays.bootstrap.api.user

import com.threedays.application.user.port.inbound.RegisterUser
import com.threedays.bootstrap.api.support.security.withUserAuthentication
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.JobOccupation
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.repository.UserRepository
import com.threedays.domain.user.vo.Gender
import com.threedays.oas.api.UsersApi
import com.threedays.oas.model.GetMyUserInfoResponse
import com.threedays.oas.model.RegisterUserRequest
import com.threedays.oas.model.TokenResponse
import com.threedays.oas.model.UserProfile
import com.threedays.support.common.base.domain.UUIDTypeId
import com.threedays.support.common.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.Year

@RestController
class UserController(
    private val registerUser: RegisterUser,
    private val userRepository: UserRepository,
) : UsersApi {

    override fun registerUser(
        xRegisterToken: String,
        registerUserRequest: RegisterUserRequest
    ): ResponseEntity<TokenResponse> {
        val result: RegisterUser.Result = RegisterUser.Command(
            name = User.Name(registerUserRequest.name),
            phoneNumber = PhoneNumber(registerUserRequest.phoneNumber),
            userGender = Gender.valueOf(registerUserRequest.profile.gender.name),
            userBirthYear = Year.of(registerUserRequest.profile.birthYear),
            userCompanyId = UUIDTypeId.from(registerUserRequest.profile.companyId),
            userJobOccupation = JobOccupation.valueOf(registerUserRequest.profile.jobOccupation.name),
            userLocationIds = registerUserRequest.profile.locationIds.map(UUIDTypeId::from),
            partnerJobOccupations = registerUserRequest.desiredPartner.jobOccupations
                .let { requestData -> requestData.map { JobOccupation.valueOf(it.name) } },
            partnerBirthYearRange = registerUserRequest.desiredPartner.birthYearRange
                ?.let { Year.of(it.start)..Year.of(it.end) },
            partnerPreferDistance = UserDesiredPartner
                .PreferDistance
                .valueOf(registerUserRequest.desiredPartner.preferDistance.name),
        ).let {
            registerUser.invoke(it)
        }

        return TokenResponse(
            result.accessToken.value,
            result.refreshToken.value,
            result.expiresIn.toInt()
        ).let {
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(it)
        }
    }

    override fun getMyUserInfo(): ResponseEntity<GetMyUserInfoResponse> =
        withUserAuthentication { userAuthentication ->
            val user: User = userRepository
                .find(userAuthentication.userId)
                ?: throw NotFoundException("User not found")

            GetMyUserInfoResponse(
                id = user.id.value,
                name = user.name.value,
                phoneNumber = user.phoneNumber.value,
                profile = UserProfile(
                    gender = com.threedays.oas.model.Gender.valueOf(user.profile.gender.name),
                    birthYear = user.profile.birthYear.value,
                    companyId = user.profile.company.id.value,
                    jobOccupation = user.profile.jobOccupation.name.let {
                        com.threedays.oas.model.JobOccupation.valueOf(
                            it
                        )
                    },
                    locationIds = user.profile.locations.map { it.id.value },
                ),
                desiredPartner = com.threedays.oas.model.UserDesiredPartner(
                    jobOccupations = user.desiredPartner.jobOccupations.map {
                        com.threedays.oas.model.JobOccupation.valueOf(
                            it.name
                        )
                    },
                    birthYearRange = user.desiredPartner.birthYearRange?.let {
                        com.threedays.oas.model.BirthYearRange(
                            it.start.value,
                            it.endInclusive.value
                        )
                    },
                    preferDistance = com.threedays.oas.model.PreferDistance.valueOf(user.desiredPartner.preferDistance.name),
                )
            ).let { ResponseEntity.ok(it) }
        }
}

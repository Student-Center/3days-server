package com.threedays.bootstrap.api.user

import com.threedays.application.user.port.inbound.RegisterUser
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.auth.vo.UserAuthentication
import com.threedays.domain.user.entity.JobOccupation
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.UserDesiredPartner
import com.threedays.domain.user.vo.Gender
import com.threedays.oas.api.UsersApi
import com.threedays.oas.model.GetMyUserInfoResponse
import com.threedays.oas.model.RegisterUserRequest
import com.threedays.oas.model.TokenResponse
import com.threedays.support.common.base.domain.UUIDTypeId
import com.threedays.support.common.security.SecurityContext
import com.threedays.support.common.security.SecurityContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.Year

@RestController
class UserController(
    private val registerUser: RegisterUser,
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

    override fun getMyUserInfo(): ResponseEntity<GetMyUserInfoResponse> {
        val securityContext: SecurityContext<UserAuthentication>? =
            SecurityContextHolder.getContext()

        println("securityContext: ${securityContext?.authentication?.userId}")
        // TODO: Implement this method

        throw NotImplementedError()
    }
}

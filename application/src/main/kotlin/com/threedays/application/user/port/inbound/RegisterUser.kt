package com.threedays.application.user.port.inbound

import com.threedays.domain.auth.entity.AccessToken
import com.threedays.domain.auth.entity.RefreshToken
import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.Company
import com.threedays.domain.user.entity.JobOccupation
import com.threedays.domain.user.entity.Location
import com.threedays.domain.user.entity.User.Name
import com.threedays.domain.user.entity.UserDesiredPartner.PreferDistance
import com.threedays.domain.user.vo.Gender
import java.time.Year

interface RegisterUser {

    fun invoke(command: Command): Result

    /**
     * 회원 가입 요청
     * @param name 이름
     * @param phoneNumber 전화번호
     * @param userGender 성별
     * @param userBirthYear 출생년도
     * @param userCompanyId 회사 ID
     * @param userJobOccupation 직업 ID
     * @param userLocationIds 지역 ID 목록
     * @param partnerBirthYearRange 파트너 출생년도 범위, null이면 제한 없음
     * @param partnerJobOccupations 파트너 직업 목록
     * @param partnerPreferDistance 파트너 선호 거리
     * @return 회원
     */
    data class Command(
        val name: Name,
        val phoneNumber: PhoneNumber,
        val userGender: Gender,
        val userBirthYear: Year,
        val userCompanyId: Company.Id,
        val userJobOccupation: JobOccupation,
        val userLocationIds: List<Location.Id>,
        val partnerBirthYearRange: ClosedRange<Year>? = null,
        val partnerJobOccupations: List<JobOccupation>,
        val partnerPreferDistance: PreferDistance
    )

    /**
     * 회원 가입 결과
     * @param accessToken 액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param expiresIn 액세스 토큰 만료 시간
     */
    data class Result(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken,
        val expiresIn: Long
    )

}

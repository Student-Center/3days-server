package com.threedays.domain.connection.entity

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.User
import com.threedays.domain.user.entity.User.Name
import com.threedays.domain.user.entity.UserProfile
import com.threedays.domain.user.entity.UserProfileImage
import com.threedays.support.common.base.domain.DomainEntity

/**
 * 매칭에 참여하는 유저
 * @param id: 참여자 식별자 ID
 * @param userId: 유저의 ID
 * @param name: 유저의 이름
 * @param phoneNumber: 유저의 전화번호
 * @param profileImages: 유저의 프로필이미지 목록
 * @param profile: 유저의 프로필
 */
data class Participant(
    override val id: User.Id,
    val name: Name,
    val phoneNumber: PhoneNumber,
    val profileImages: List<UserProfileImage> = emptyList(),
    val profile: UserProfile,
) : DomainEntity<Participant, User.Id>() {

    companion object {
        fun from(user: User): Participant {
            return Participant(
                id = user.id,
                name = user.name,
                phoneNumber = user.phoneNumber,
                profileImages = user.profileImages,
                profile = user.profile,
            )
        }
    }
}

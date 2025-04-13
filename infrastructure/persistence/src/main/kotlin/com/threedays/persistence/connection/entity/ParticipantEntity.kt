package com.threedays.persistence.connection.entity

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.connection.entity.Participant
import com.threedays.domain.user.entity.User
import com.threedays.persistence.user.entity.UserJpaEntity

data class ParticipantEntity(
    val userJpaEntity: UserJpaEntity
) {
    fun toDomain(): Participant {
        return Participant(
            id = User.Id(userJpaEntity.id),
            name = User.Name(userJpaEntity.name),
            phoneNumber = PhoneNumber(userJpaEntity.phoneNumber),
            profileImages = userJpaEntity.profileImages.map { it.toDomain() },
            profile = userJpaEntity.profile.toDomainEntity(),
        )
    }
}


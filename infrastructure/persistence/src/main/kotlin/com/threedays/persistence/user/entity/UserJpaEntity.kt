package com.threedays.persistence.user.entity

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.User
import com.threedays.persistence.user.entity.UserDesiredPartnerJpaEntity.Companion.toJpaEntity
import com.threedays.persistence.user.entity.UserProfileImageJpaEntity.Companion.toJpaEntity
import com.threedays.persistence.user.entity.UserProfileJpaEntity.Companion.toJpaEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "`users`")
class UserJpaEntity(
    id: UUID,
    name: String,
    phoneNumber: String,
    profileImages: List<UserProfileImageJpaEntity>,
    profile: UserProfileJpaEntity,
    desiredPartner: UserDesiredPartnerJpaEntity,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "name", nullable = false)
    var name: String = name
        private set

    @Column(name = "phone_number", nullable = false, unique = true)
    var phoneNumber: String = phoneNumber
        private set

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @OrderColumn(name = "image_order")
    var profileImages: List<UserProfileImageJpaEntity> = profileImages
        private set

    @OneToOne(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
    )
    var profile: UserProfileJpaEntity = profile
        private set

    @OneToOne(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
    )
    var desiredPartner: UserDesiredPartnerJpaEntity = desiredPartner
        private set

    companion object {

        fun User.toJpaEntity() = UserJpaEntity(
            id = id.value,
            name = name.value,
            phoneNumber = phoneNumber.value,
            profileImages = profileImages.map { it.toJpaEntity() },
            profile = profile.toJpaEntity(),
            desiredPartner = desiredPartner.toJpaEntity(),
        )
    }

    fun toDomainEntity(): User {
        return User(
            id = User.Id(id),
            name = User.Name(name),
            profileImages = profileImages.map { it.toDomain() },
            phoneNumber = PhoneNumber(phoneNumber),
            profile = profile.toDomainEntity(),
            desiredPartner = desiredPartner.toDomainEntity(),
        )
    }

}

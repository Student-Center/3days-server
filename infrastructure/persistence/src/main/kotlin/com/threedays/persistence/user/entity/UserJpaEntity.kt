package com.threedays.persistence.user.entity

import com.threedays.domain.auth.vo.PhoneNumber
import com.threedays.domain.user.entity.User
import com.threedays.persistence.user.entity.UserDesiredPartnerJpaEntity.Companion.toJpaEntity
import com.threedays.persistence.user.entity.UserProfileJpaEntity.Companion.toJpaEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "`users`")
class UserJpaEntity(
    id: UUID,
    name: String,
    phoneNumber: String,
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
            profile = profile.toJpaEntity(),
            desiredPartner = desiredPartner.toJpaEntity(),
        )
    }

    fun toDomainEntity(): User {
        return User(
            id = User.Id(id),
            name = User.Name(name),
            phoneNumber = PhoneNumber(phoneNumber),
            profile = profile.toDomainEntity(),
            desiredPartner = desiredPartner.toDomainEntity(),
        )
    }

}

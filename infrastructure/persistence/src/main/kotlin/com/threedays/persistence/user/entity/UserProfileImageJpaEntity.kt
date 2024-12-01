package com.threedays.persistence.user.entity

import com.threedays.domain.user.entity.UserProfileImage
import com.threedays.support.common.base.domain.UUIDTypeId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.net.URI
import java.util.*

@Entity
@Table(name = "user_profile_images")
class UserProfileImageJpaEntity(
    id: UUID,
    extension: UserProfileImage.Extension,
    url: String,
) {

    @Id
    var id: UUID = id
        private set

    @Enumerated(EnumType.STRING)
    var extension: UserProfileImage.Extension = extension
        private set

    var url: String = url
        private set

    companion object {

        fun UserProfileImage.toJpaEntity() = UserProfileImageJpaEntity(
            id = id.value,
            extension = extension,
            url = url.toString(),
        )
    }

    fun toDomain() = UserProfileImage(
        id = UUIDTypeId.from(id),
        extension = extension,
        url = URI.create(url).toURL(),
    )

}

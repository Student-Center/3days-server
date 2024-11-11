package com.threedays.persistence.user.entity

import com.threedays.domain.user.entity.ProfileWidget
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class ProfileWidgetJpaEmbeddable(
    @Enumerated(EnumType.STRING)
    val type: ProfileWidget.Type,
    val content: String,
) {

    companion object {

        fun ProfileWidget.toJpaEmbeddable() = ProfileWidgetJpaEmbeddable(
            type = type,
            content = content,
        )

    }

    fun toValueObject() = ProfileWidget(
        type = type,
        content = content,
    )

}

package com.threedays.domain.user.vo

enum class Gender {
    MALE,
    FEMALE;

    fun opposite(): Gender {
        return when (this) {
            MALE -> FEMALE
            FEMALE -> MALE
        }
    }

}

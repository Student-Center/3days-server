package com.threedays.domain.user.vo

enum class Gender {
    MAN,
    WOMAN;

    fun opposite(): Gender {
        return when (this) {
            MAN -> WOMAN
            WOMAN -> MAN
        }
    }

}

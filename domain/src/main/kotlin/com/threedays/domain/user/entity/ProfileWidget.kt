package com.threedays.domain.user.entity

data class ProfileWidget(
    val type: Type,
    val comment: String,
) {

    enum class Type {
        HOBBY,
        STYLE,
        BODY_TYPE,
        MUSIC,
        DRAMA_MOVIE,
        BOOK,
        MBTI,
        RELIGION,
        SMOKING,
        DRINKING,
    }

}

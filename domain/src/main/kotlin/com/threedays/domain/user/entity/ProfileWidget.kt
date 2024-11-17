package com.threedays.domain.user.entity

data class ProfileWidget(
    val type: Type,
    val content: String,
) {

    enum class Type {
        HOBBY,
        STYLE,
        MBTI,
        MUSIC,
        BODY_TYPE,
        FOOD,
        MOVIE,
        DRAMA,
        BOOK,
        TRAVEL,
        DRINKING,
        MARRIAGE,
        RELIGION,
        SMOKING,
    }

}

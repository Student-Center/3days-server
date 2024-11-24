package com.threedays.domain.user.exception

import com.threedays.support.common.base.exception.CustomException

sealed class UserException(
    codeNumber: Int,
    override val message: String = DEFAULT_MESSAGE,
) : CustomException("USER", codeNumber, message) {

    data class ProfileImageUploadFailedException(
        override val message: String = "프로필 이미지 업로드에 실패했습니다.",
    ) : UserException(2010, message)

}

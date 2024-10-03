package com.threedays.support.common.base.exception

abstract class CustomException(
    val type: String = DEFAULT_CODE_PREFIX,
    code: Int,
    override val message: String = DEFAULT_MESSAGE,
) : RuntimeException(message) {

    val code: String = "$type-${
        code.toString().padStart(DEFAULT_CODE_NUMBER_LENGTH, DEFAULT_CODE_NUMBER_PAD_CHAR)
    }"

    companion object {

        const val DEFAULT_CODE_NUMBER_LENGTH = 3
        const val DEFAULT_CODE_NUMBER_PAD_CHAR = '0'
        const val DEFAULT_CODE_PREFIX = "UNKNOWN"
        const val DEFAULT_MESSAGE = "알 수 없는 오류가 발생했습니다."

    }

}

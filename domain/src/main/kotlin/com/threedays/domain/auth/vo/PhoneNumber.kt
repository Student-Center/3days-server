package com.threedays.domain.auth.vo

@JvmInline
value class PhoneNumber(val value: String) {

    companion object {

        private val regex = Regex("^01[0-9]{8,9}\$")
    }

    init {
        require(value.matches(regex)) { "휴대폰 번호는 01으로 시작하고 10~11자리 숫자로 구성되어야 합니다." }
    }

}

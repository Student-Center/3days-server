package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.User

interface DeleteMyUser {

    fun invoke(command: Command)

    /**
     * 회원 가입 요청
     * @param userId 회원 ID
     */
    data class Command(
        val userId: User.Id,
    )
}

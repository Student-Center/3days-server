package com.threedays.application.user.port.inbound

import com.threedays.domain.user.entity.User

interface DeleteMyUser {

    fun invoke(command: Command)

    /**
     * 회원 삭제 요청
     * @param userId 삭제할 회원 ID
     */
    data class Command(
        val userId: User.Id,
    )
}

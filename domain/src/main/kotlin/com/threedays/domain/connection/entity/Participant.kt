package com.threedays.domain.connection.entity

import com.threedays.domain.user.entity.User
import com.threedays.support.common.base.domain.DomainEntity

/**
 * 매칭에 참여하는 유저
 * @param id: 유저의 ID
 * @param connectionResponse: 매칭에 대한 응답
 */
data class Participant(
    override val id: User.Id,
    val connectionResponse: ConnectionResponse = ConnectionResponse.NO_RESPONSE
) : DomainEntity<Participant, User.Id>() {

    /**
     * 유저의 매칭에 대한 응답
     */
    enum class ConnectionResponse {
        NO_RESPONSE,    // 응답하지 않음
        KEEP_CONNECT,   // 커넥션 유지
        DISCONNECT,     // 연결 끊기
    }

}

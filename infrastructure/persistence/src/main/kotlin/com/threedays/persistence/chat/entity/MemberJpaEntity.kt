package com.threedays.persistence.chat.entity

import com.threedays.domain.chat.entity.Member
import com.threedays.domain.user.entity.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "chat_members")
class MemberJpaEntity(
    id: UUID,
    joinedAt: LocalDateTime,
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "joined_at", nullable = false)
    var joinedAt: LocalDateTime = joinedAt
        private set

    companion object {

        fun toJpaEntity(member: Member): MemberJpaEntity {
            return MemberJpaEntity(
                id = member.id.value,
                joinedAt = member.joinedAt
            )
        }
    }

    fun toDomainEntity(): Member {
        return Member(
            id = User.Id(id),
            joinedAt = joinedAt
        )
    }

}

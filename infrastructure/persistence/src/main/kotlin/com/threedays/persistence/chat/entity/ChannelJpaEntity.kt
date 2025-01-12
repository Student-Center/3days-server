package com.threedays.persistence.chat.entity

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.connection.entity.Connection
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "channels")
class ChannelJpaEntity(
    id: UUID,
    connectionId: UUID,
    members: List<MemberJpaEntity>,
    createdAt: LocalDateTime
) {

    @Id
    var id: UUID = id
        private set

    @Column(name = "connection_id", nullable = false)
    var connectionId: UUID = connectionId
        private set

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JoinColumn(name = "channel_id", nullable = false)
    var members: List<MemberJpaEntity> = members
        private set

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = createdAt
        private set

    companion object {

        fun toJpaEntity(channel: Channel): ChannelJpaEntity {
            return ChannelJpaEntity(
                id = channel.id.value,
                connectionId = channel.connectionId.value,
                members = channel.members.map { MemberJpaEntity.toJpaEntity(it) },
                createdAt = channel.createdAt
            )
        }
    }

    fun toDomainEntity(): Channel {
        return Channel(
            id = Channel.Id(id),
            connectionId = Connection.Id(connectionId),
            members = members.map { it.toDomainEntity() },
            createdAt = createdAt
        )
    }

}

package com.threedays.persistence.chat.adapter

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.repository.ChannelRepository
import com.threedays.persistence.chat.repository.ChannelJpaRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
@Transactional
class ChannelPersistenceAdapter(
    private val channelJpaRepository: ChannelJpaRepository,
) : ChannelRepository {

    override fun findById(id: Channel.Id): Channel? {
        return channelJpaRepository
            .findById(id.value)
            .map { it.toDomainEntity() }
            .orElse(null)
    }

}

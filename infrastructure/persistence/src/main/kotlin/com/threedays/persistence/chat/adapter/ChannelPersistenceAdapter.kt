package com.threedays.persistence.chat.adapter

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.repository.ChannelRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
@Transactional
class ChannelPersistenceAdapter : ChannelRepository {

    override fun findById(id: Channel.Id): Channel? {
        TODO("Not yet implemented")
    }

}

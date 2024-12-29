package com.threedays.domain.chat.repository

import com.threedays.domain.chat.entity.Channel

interface ChannelRepository {

    fun findById(id: Channel.Id): Channel?

}

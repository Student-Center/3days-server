package com.threedays.domain.chat.repository

import com.threedays.domain.chat.entity.Channel
import com.threedays.domain.chat.entity.Message
import com.threedays.support.common.base.domain.QueryRepository

interface MessageQueryRepository : QueryRepository<Message, Message.Id> {

    fun scrollByChannelId(
        channelId: Channel.Id,
        next: Message.Id?,
        limit: Int,
    ): Pair<List<Message>, Message.Id?>

}

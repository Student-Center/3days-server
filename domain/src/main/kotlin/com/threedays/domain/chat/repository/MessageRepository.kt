package com.threedays.domain.chat.repository

import com.threedays.domain.chat.entity.Message

interface MessageRepository {

    fun save(message: Message)

}

package com.threedays.aws.dynamodb.chat

import com.threedays.domain.chat.entity.Message
import com.threedays.domain.chat.repository.MessageRepository
import org.springframework.stereotype.Repository

@Repository
class MessageDynamodbRepository: MessageRepository {

    override fun save(message: Message) {
        TODO("Not yet implemented")
    }


}

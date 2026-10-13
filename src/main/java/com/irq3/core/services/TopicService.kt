package com.irq3.core.services

import com.irq3.database.TopicDAO
import com.irq3.models.Topic
import java.time.OffsetDateTime

class TopicService(val topicDao: TopicDAO) {

    fun getRandomTopic(): Topic{
        return topicDao.getRandomTopic() ?: Topic(0, "Ile masz drzwi w domu", OffsetDateTime.now(), 0)
    }
    fun createTopic(topic: String, adminId: Long){
        topicDao.createTopic(topic,adminId)
    }

}
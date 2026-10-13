package com.irq3.discord.utils

import com.irq3.infrastucture.EnvController
import org.slf4j.LoggerFactory

class AdminChecker {
    companion object{
        private val owner = EnvController.getData("OWNER").toLong()
        private val admins = EnvController.getData("ADMIN").split(",").map{it.toLong()}
        private val logger = LoggerFactory.getLogger(this.javaClass)


        fun isAdmin(id: Long): Boolean{
            logger.info("{} was checked if he is admin",id)
            return admins.contains(id)
        }
        fun isOwner(id: Long): Boolean{
            logger.info("{} was checked if he is owner",id)
            return owner==id
        }

    }

}
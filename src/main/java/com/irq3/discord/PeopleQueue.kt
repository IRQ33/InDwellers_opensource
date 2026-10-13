package com.irq3.discord

import com.google.gson.Gson
import com.irq3.INDWELLERS
import com.irq3.core.DIManager
import com.irq3.discord.utils.LimitArray
import com.irq3.models.User
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.emptyArray
import kotlin.random.Random

object PeopleQueue {
    val userList: MutableList<User> = ArrayList()
    val mutex = Mutex()
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val lastSpeakers = ConcurrentHashMap<User, LimitArray<User>>()
    suspend fun addPerson(usr : User): Boolean{
        mutex.withLock {
            if(userList.contains(usr)){
                return false
            }
            userList.add(usr)
            logger.info("New person added to queue with {}, people in queue {} ",usr.id,userList.size)
            return true
        }
    }
    suspend fun remove(usr: User){
        logger.info("New person deleted from queue with {}, people in queue {} ",usr.id,userList.size)
        mutex.withLock {
            userList.remove(usr)
        }
    }

    init {
        val file = File("people.json")
        if(file.exists()){
            val text = file.readText()
            try {
                val list = Gson().fromJson(text, Array<User>::class.java)
                userList.addAll(list)
            }catch (e: Exception){
                logger.error("Big error happened with json. {}",e.toString())
                userList.clear()
            }
        }else{
            file.createNewFile()
        }
    }

    suspend fun randomPeople(): Array<User>{

        return mutex.withLock {
            if (userList.size < 2) {
                return@withLock emptyArray<User>()
            }
            logger.info("Queue started working, in queue there is {} people", userList.size)

            val idx = Random.nextInt(0, userList.size)
            val p1 = userList.removeAt(idx)
            val idx2 = Random.nextInt(0, userList.size)
            val p2 = userList.removeAt(idx2)

            val blockManager = DIManager.conversationService

            if(INDWELLERS.DEV){

                lastSpeakers.getOrPut(p1) { LimitArray(3) }.addElement(p2)
                lastSpeakers.getOrPut(p2) { LimitArray(3) }.addElement(p1)

                if(blockManager.isBlocked(p1.id!!,p2.id!!)|| blockManager.isBlocked(p2.id,p1.id)){
                    return@withLock emptyArray()
                }
                arrayOf(p1,p2)
            }else{
                if(blockManager.isBlocked(p1.id!!,p2.id!!)|| blockManager.isBlocked(p2.id,p1.id)){
                    return@withLock emptyArray()
                }
                if(lastSpeakers[p1] !=null && lastSpeakers[p2]==null){
                    if(lastSpeakers[p1]!!.contains(p2)){
                        return@withLock emptyArray()
                    }
                }

                lastSpeakers.getOrPut(p1) { LimitArray(3) }.addElement(p2)
                lastSpeakers.getOrPut(p2) { LimitArray(3) }.addElement(p1)

                arrayOf(p1,p2)
            }
        }
    }

    fun stop(){
        val file = File("people.json")
        val json = Gson().toJson(userList)
        file.writeText(json)
    }

}
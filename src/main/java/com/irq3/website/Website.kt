package com.irq3.website

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.irq3.core.DIManager
import com.irq3.core.Event
import com.irq3.core.EventBus
import com.irq3.discord.PeopleQueue
import com.irq3.infrastucture.EnvController
import com.irq3.website.discord.DiscordUtils.Companion.verifyDiscordSignature
import com.irq3.website.discord.UserWebhook
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


object Website {
    var status: BOT_STATUS = BOT_STATUS.OFFLINE

    fun init(){
        embeddedServer(Netty, port = 8000) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }
            routing {
                get ("/") {
                    call.respond(mapOf("status" to status.name))
                }
                get("online"){
                    val people = DIManager.conversationService.countConversations()+ PeopleQueue.userList.size
                    call.respond(mapOf("online" to people))
                }
                post("/webhook") {
                    val signature = call.request.headers["X-Signature-Ed25519"]
                    val timestamp = call.request.headers["X-Signature-Timestamp"]
                    val rawBody = call.receiveText()

                    if (signature == null || timestamp == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Missing headers")
                        return@post
                    }
                    val publicKeyHex = EnvController.getData("public_key")
                    val isValid = verifyDiscordSignature(publicKeyHex, signature, timestamp, rawBody)

                    if (!isValid) {
                        call.respond(HttpStatusCode.Unauthorized, "Invalid signature")
                        return@post
                    }

                    val json = JsonParser.parseString(rawBody).asJsonObject

                    when (json.get("type")?.asInt) {
                        0->{
                            call.respond(204)
                        }
                        else -> {
                            val body = Gson().fromJson(rawBody, UserWebhook::class.java)
                            EventBus.emit(Event.GreetingMessageWebhook(body))
                            call.respond(HttpStatusCode.OK)
                        }
                    }
                }

            }
        }.start(wait = true)
    }
}
enum class BOT_STATUS{
    OFFLINE,ONLINE
}
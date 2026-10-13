package com.irq3.infrastucture

import java.io.File

object EnvController {
    private val args = HashMap<String, String>()

    init{
        val file = File(".env")
        if(!file.exists() || !file.canRead()){
            throw RuntimeException("File cannot be load")
        }

        val lines = file.readLines()
        lines.forEach { line->
            if(line.startsWith("#"))
                return@forEach

            val stuffs = line.split("=", limit = 2)
            require(stuffs.size == 2) { "Bad .env line: $line" }

            args[stuffs[0].trim()] = stuffs[1].trim()
        }
    }

    fun getData(key : String): String{
        return args[key] ?: throw RuntimeException("Missing env key: $key")
    }
}
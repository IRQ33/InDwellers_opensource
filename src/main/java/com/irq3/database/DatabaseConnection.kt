package com.irq3.database

import com.irq3.infrastucture.EnvController
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseConnection {

    fun init(){
        val name = EnvController.getData("DB_NAME")
        val url = EnvController.getData("DB_URL")+name
        val user = EnvController.getData("DB_USER")
        val pswd = EnvController.getData("DB_PASS")

        val flyway = Flyway.configure()
            .dataSource(url,user,pswd)
            .load()

        flyway.migrate()


        val config = HikariConfig().apply {
            jdbcUrl = url
            username = user
            password = pswd
            maximumPoolSize = 10
        }
        val db = Database.connect(HikariDataSource(config))

        val closed = transaction(db) {
             connection.isClosed
        }
        if(closed){
            throw Exception("Database doesn't work")
        }

    }

}
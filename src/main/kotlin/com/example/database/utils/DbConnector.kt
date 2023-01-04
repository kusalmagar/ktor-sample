package com.example.database.utils

import org.ktorm.database.Database

object DbConnector {
    private const val baseUrl: String = "jdbc:mysql://localhost:8080/"
    private const val dbDriver: String = "com.mysql.cj.jdbc.Driver"
    private const val _userName: String = "root"
    private const val _password: String = "root"

    private val dbInstance: Database? = null

    fun getDataBaseInstance(dbName: String): Database {
        return dbInstance ?: Database.connect(
            url = baseUrl + dbName,
            driver = dbDriver,
            user = _userName,
            password = _password
        )

    }

}
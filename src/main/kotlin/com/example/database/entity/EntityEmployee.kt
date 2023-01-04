package com.example.database.entity

import org.ktorm.schema.Table
import org.ktorm.schema.float
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object EntityEmployee : Table<Nothing>(tableName = "employee") {
    val id = int("id").primaryKey()
    val firstName = varchar("first_name")
    val lastName = varchar("last_name")
    val age = int("age")
    val salary = float("salary")
    val email = varchar("email")
    val position = varchar("position")


}
package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val salary: Double,
    val email: String,
    val position: String
)

val employees = mutableListOf<Employee>(Employee("1", "John", "Doe", 32, 60000.00, "john@gmail.com", "Product Manager"))

package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Employees(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val salary: Float,
    val email: String,
    val position: String
)

@Serializable
data class Employee(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val salary: Float,
    val email: String,
    val position: String
)



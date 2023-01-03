package com.example.plugins

import com.example.model.Employee
import com.example.routes.employeeRouting
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        employeeRouting()
    }
}

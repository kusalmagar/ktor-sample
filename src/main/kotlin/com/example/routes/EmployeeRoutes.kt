package com.example.routes

import com.example.model.Employee
import com.example.model.employees
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.employeeRouting() {
    route("/employees") {
        get {
            if (employees.isNotEmpty()) {
                call.respond(employees)
            } else {
                call.respondText("No employees found", status = HttpStatusCode.OK)
            }
        }
        get("{id}") {
            val id =
                call.parameters["id"] ?: return@get call.respondText("id missing", status = HttpStatusCode.BadRequest)
            val employee = employees.find { it.id == id } ?: return@get call.respondText(
                "No employee found with given id",
                status = HttpStatusCode.NotFound
            )
            call.respond(employee)
        }
        post {
            val employee = call.receive<Employee>()
            employees.add(employee)
            call.respondText("employee added successfully", status = HttpStatusCode.Created)

        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (employees.removeIf { it.id == id }) {
                call.respondText("employee removed", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("not found", status = HttpStatusCode.NotFound)
            }


        }
    }
}
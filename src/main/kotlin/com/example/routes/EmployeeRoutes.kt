package com.example.routes

import com.example.database.utils.DbConnector
import com.example.database.entity.EntityEmployee
import com.example.database.utils.Response
import com.example.model.Employee
import com.example.model.Employees
//import com.example.model.employees
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Route.employeeRouting() {

    val dbInstance = DbConnector.getDataBaseInstance("employeeMgmt")
    route("/employees") {
        get {
            val employee =
                dbInstance.from(EntityEmployee).select().map {
                    Employees(
                        it[EntityEmployee.id]!!,
                        it[EntityEmployee.firstName]!!,
                        it[EntityEmployee.lastName]!!,
                        it[EntityEmployee.age]!!,
                        it[EntityEmployee.salary]!!,
                        it[EntityEmployee.email]!!,
                        it[EntityEmployee.position]!!
                    )
                }
            if (employee.isNotEmpty()) {
                call.respond(status = HttpStatusCode.OK, employee)
            } else {
                call.respond(
                    HttpStatusCode.NotFound, Response(success = false, response = "no data found")
                )
            }
//            call.respond(HttpStatusCode.InternalServerError, mapOf("response" to "unable to connect to server"))
        }
        get("{id}") {
            val id: Int =
                call.parameters["id"]?.toInt() ?: return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    Response(success = false, response = "id not provided")
                )
            val employee =
                dbInstance.from(EntityEmployee).select().where(EntityEmployee.id eq id).map {
                    Employees(
                        it[EntityEmployee.id]!!,
                        it[EntityEmployee.firstName]!!,
                        it[EntityEmployee.lastName]!!,
                        it[EntityEmployee.age]!!,
                        it[EntityEmployee.salary]!!,
                        it[EntityEmployee.email]!!,
                        it[EntityEmployee.position]!!
                    )
                }

            if (employee.isEmpty()) {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    Response(success = false, response = "no employee found with given id : $id")
                )

            } else {
                call.respond(status = HttpStatusCode.OK, employee)

            }
        }

        route("/add") {
            post {
                val employee: Employee = call.receive()
                val checkEmail = dbInstance.from(EntityEmployee).select()
                    .where(EntityEmployee.email eq employee.email)
                if (checkEmail.totalRecords == 1) {
                    call.respond(
                        status = HttpStatusCode.Conflict, Response(success = false, response = "email already exists")
                    )
                } else {
                    val idGenerated = dbInstance.insertAndGenerateKey(EntityEmployee) {
                        set(it.firstName, employee.firstName)
                        set(it.lastName, employee.lastName)
                        set(it.age, employee.age)
                        set(it.salary, employee.salary)
                        set(it.email, employee.email)
                        set(it.position, employee.position)

                    }

                    if (idGenerated == Int) {
                        call.respond(
                            status = HttpStatusCode.Conflict,
                            Response(success = false, response = "failed to create employee")

                        )
                    } else {
                        val createdUser = dbInstance.from(EntityEmployee).select()
                            .where(EntityEmployee.id eq idGenerated.toString().toInt()).map {
                                Employees(
                                    it[EntityEmployee.id]!!,
                                    it[EntityEmployee.firstName]!!,
                                    it[EntityEmployee.lastName]!!,
                                    it[EntityEmployee.age]!!,
                                    it[EntityEmployee.salary]!!,
                                    it[EntityEmployee.email]!!,
                                    it[EntityEmployee.position]!!
                                )
                            }
                        if (createdUser.isEmpty()) {
                            call.respond(
                                status = HttpStatusCode.Conflict,
                                Response(success = false, response = "failed to create employee")
                            )

                        } else {
                            call.respond(status = HttpStatusCode.Created, createdUser)
                        }
                    }
                }

                call.respond(
                    status = HttpStatusCode.BadRequest,
                    Response(success = false, response = "insufficient employee details")
                )
            }
        }



        put("/update/{id}") {
            val employee: Employee = call.receive()
            val id = call.parameters["id"]?.toInt() ?: return@put call.respond(
                status = HttpStatusCode.NotFound,
                Response(success = false, response = "id not provided")
            )
            val checkUser = dbInstance.from(EntityEmployee).select()
                .where(EntityEmployee.id eq id)
            if (checkUser.totalRecords == 1) {
                val checkEmail = dbInstance.from(EntityEmployee).select()
                    .where(EntityEmployee.email eq employee.email)
                if (checkEmail.totalRecords == 1) {
                    call.respond(
                        status = HttpStatusCode.Conflict, Response(success = false, response = "email already exists")
                    )
                } else {
                    val update = dbInstance.update(EntityEmployee) {
                        set(it.firstName, employee.firstName)
                        set(it.lastName, employee.lastName)
                        set(it.age, employee.age)
                        set(it.salary, employee.salary)
                        set(it.email, employee.email)
                        set(it.position, employee.position)
                        where {
                            it.id eq id
                        }
                    }
                    if (update == 1) {
                        call.respond(
                            status = HttpStatusCode.Accepted,
                            dbInstance.from(EntityEmployee).select().where(EntityEmployee.id eq id).map {
                                Employees(
                                    it[EntityEmployee.id]!!,
                                    it[EntityEmployee.firstName]!!,
                                    it[EntityEmployee.lastName]!!,
                                    it[EntityEmployee.age]!!,
                                    it[EntityEmployee.salary]!!,
                                    it[EntityEmployee.email]!!,
                                    it[EntityEmployee.position]!!
                                )
                            }
                        )
                    }
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        Response(success = false, response = "update failed")
                    )
                }
            } else {
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    Response(success = false, response = "unauthorized access")
                )
            }


        }



        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@delete call.respond(
                status = HttpStatusCode.BadRequest,
                mapOf("response" to "id not provided")
            )
            val deleteEmployee = dbInstance.delete(EntityEmployee) { it.id eq id }
            if (deleteEmployee == 1) {
                call.respond(
                    status = HttpStatusCode.Accepted,
                    Response(
                        success = true, response = "employee delete success"
                    )
                )
            } else {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    Response(success = false, response = "employee not found")
                )
            }

        }
    }


}
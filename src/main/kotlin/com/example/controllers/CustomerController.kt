package com.example.controllers

import com.example.models.Customer
import com.example.services.CustomerService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureCustomerRouting(){

    val customerService = CustomerService()
    routing {
        get{
            call.respond(customerService.getAllCustomers())
        }
        get("/{id}"){
            call.respond(customerService.getCustomerById(Integer.parseInt(call.parameters["id"])))
        }
        post("/customer") {
            val customer = call.receive<Customer>()
            customerService.addCustomer(customer)
        }
        delete("/delete/{id}"){
            customerService.deleteCustomer(Integer.parseInt(call.parameters["id"]))
        }
    }

}
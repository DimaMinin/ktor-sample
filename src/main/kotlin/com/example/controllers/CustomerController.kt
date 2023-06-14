package com.example.controllers

import com.example.dtos.ReceiveFeedback
import com.example.models.Customer
import com.example.services.*
import com.example.utils.getUsernameFromToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject


fun Routing.configureCustomerRouting() = route("/customers"){

    config()

}

private fun Route.config(){

    val customerService by inject<CustomerService>()
    val productService by inject<ProductService>()
    val cartService by inject<CartService>()
    val roleService by inject<RoleService>()
    val feedbackService by inject<FeedbackService>()


    get{
        call.respond(customerService.getAllCustomers())
    }

    get("/roles") {
        val roles = roleService.getAllRoles()
        call.respond(roles)
    }

    authenticate("user") {
        get("/info"){
            val username = getUsernameFromToken(call)
            val customer = customerService.getCustomerByUsername(username)
            call.respond(customer!!)
        }
    }

    authenticate("user"){
        get("/cart"){
            val username = getUsernameFromToken(call)
            call.respond(cartService.getCart(username))
        }
    }

    authenticate("admin"){
        post("/cart/{productId}"){
            val username = getUsernameFromToken(call)
            val productId = call.parameters.getOrFail<Int>("productId")
            cartService.addProductInCart(username, productId)
            call.respond(cartService.getCart(username))
        }
    }

    authenticate("admin"){
        get("/feedbacks"){
            call.respond(feedbackService.getAllFeedbacks())
        }
    }

    authenticate("admin"){
        post("/feedbacks/{productId}"){
            val message = call.receive<ReceiveFeedback>().message
            val productId = call.parameters.getOrFail<Int>("productId")
            val username = getUsernameFromToken(call)
            feedbackService.addFeedback(message, username, productId)
            call.respond(HttpStatusCode.NoContent)
        }
    }



    post {
        val (_, name, surname, username, password, role) = call.receive<Customer>()
        if (customerService.getCustomerByUsername(username) == null) {
            customerService.addCustomer(name, surname, username, password, role)
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond("user with username = $username, already exists")
        }
    }
    delete("/{id}"){
        val customerId = call.parameters.getOrFail<Int>("id")
        customerService.deleteCustomer(customerId)
    }

}
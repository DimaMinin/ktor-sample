package com.example.controllers

import com.example.dto.auth.LoginRequest
import com.example.dto.auth.TokenDto
import com.example.services.auth.AuthService
import com.example.services.auth.IAuthService
import com.example.services.customer.CustomerService
import com.example.services.customer.ICustomerService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureAuthRouting() = route("/auth") {

    config()

}

private fun Route.config() {

    val customerService by inject<ICustomerService>()
    val authService by inject<IAuthService>()

    post("/login") {

        val customerToLogin = call.receive<LoginRequest>()
        val customer = customerService.getCustomerByUsername(customerToLogin.username)

        if (authService.checkPassword(customerToLogin.password, customerToLogin.username)){

            val token = TokenDto(authService.createToken(customerToLogin.username, customer.role)!!)
            call.respond(token)

        } else {

            call.respond("bad password or username")

        }

    }

}
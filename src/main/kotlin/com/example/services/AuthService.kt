package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dto.auth.PropertiesDto
import org.koin.core.component.KoinComponent
import java.util.*
import org.mindrot.jbcrypt.BCrypt

class AuthService(private val properties: PropertiesDto) : KoinComponent{

    fun createToken(username: String, role: String): String? {

        val token = JWT.create()
            .withAudience(properties.jwtAudience)
            .withIssuer(properties.jwtIssuer)
            .withClaim("username", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(properties.jwtSecret))

        return token

    }

    fun checkPassword(receivePassword: String, correctPassword: String): Boolean {

        val isPasswordCorrect = BCrypt.checkpw(receivePassword, correctPassword)
        return isPasswordCorrect

    }

}
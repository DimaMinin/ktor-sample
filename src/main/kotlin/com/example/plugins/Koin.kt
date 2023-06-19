package com.example.plugins

import com.example.services.*
import com.example.utils.properties
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

val customerModule = module{
    single(createdAtStart = true) { CustomerService() }
}

val productModule = module{
    single { ProductService() }
}

val cartModule = module{
    single { CartService() }
}

val roleModule = module{
    single { RoleService() }
}

val feedbackModule = module{
    single { FeedbackService() }
}



fun Application.configureKoin() = install(Koin) {

    val authModule = module{
        single { AuthService(properties()) }
    }

    modules(authModule, roleModule, customerModule, productModule, cartModule, feedbackModule)
}
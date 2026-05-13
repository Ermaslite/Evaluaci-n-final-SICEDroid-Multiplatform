package com.example.sicedroidmultiplatform.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object KtorClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        // Nota: El manejo de cookies se puede agregar aquí si es necesario para mantener la sesión
        // install(HttpCookies)
        
        defaultRequest {
            url("https://sicenet.surguanajuato.tecnm.mx/")
        }
    }
}

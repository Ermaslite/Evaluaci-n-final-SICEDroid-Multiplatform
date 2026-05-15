package com.example.sicedroidmultiplatform.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

object SicenetService {
    val client = HttpClient {
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
        
        defaultRequest {
            url("https://sicenet.surguanajuato.tecnm.mx/")
            header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            header("Accept", "text/xml, application/xml, text/html")
        }
    }
}

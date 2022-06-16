package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.jackson.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        install(ContentNegotiation) {
            jackson()
        }
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
            outputFormat = HTMLOutputFormat.INSTANCE
        }
        registerMainRoutes()
        configureSerialization()

    }.start(wait = true)
}

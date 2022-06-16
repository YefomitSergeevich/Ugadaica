package com.example.plugins

import io.ktor.jackson.*
import com.fasterxml.jackson.databind.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.configureSerialization() {

    routing {
        get("/json/jackson") {
                call.respond(mapOf("hello" to "world"))
            }
    }
}

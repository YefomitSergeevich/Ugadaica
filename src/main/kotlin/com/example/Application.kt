package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.plugins.Swagger.configureSerialization
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.jackson.*
import kotlin.reflect.KType

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
    //embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        install(ContentNegotiation) {
            jackson()
        }
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
            outputFormat = HTMLOutputFormat.INSTANCE
        }
        install(OpenAPIGen) {
            // basic info
            info {
                version = "0.0.1"
                title = "Test API"
                description = "The Test API"
                contact {
                    name = "Support"
                    email = "support@test.com"
                }
            }
            // describe the server, add as many as you want
            server("http://localhost:8080/") {
            //server("https://fgfjdhsaryhdb.herokuapp.com/") {
                description = "Test server"
            }
            //optional custom schema object namer
            replaceModule(DefaultSchemaNamer, object : SchemaNamer {
                val regex = Regex("[A-Za-z0-9_.]+")
                override fun get(type: KType): String {
                    return type.toString().replace(regex) { it.value.split(".").last() }
                        .replace(Regex(">|<|, "), "_")
                }
            })
        }
        configureSerialization()
        registerMainRoutes()

    }.start(wait = true)
}

package com.example.plugins

import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.mapping.OpenAPIName
import com.papsign.ktor.openapigen.annotations.parameters.HeaderParam
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.route.apiRouting
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*


object Swagger {
    fun Application.configureSerialization() {
        routing {
            get("/openapi.json") {
                call.respond(application.openAPIGen.api.serialize())
            }
            get("/swagger-ui") {
                call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
            }
        }
        apiRouting {
            route("header") {
                get<NameParam, NameResponse>(
                    info("Header Param Endpoint", "This is a Header Param Endpoint"),
                    example = NameResponse("Mister")
                ) { params ->
                    respond(NameResponse("Hi, ${params.name}!"))
                }
            }
            route("inline").get<StringParam, StringResponse>(
                info("String Param Endpoint", "This is a String Param Endpoint"), // A Route module that describes an endpoint, it is optional
                example = StringResponse("Hi")
            ) { params ->
                respond(StringResponse(params.a))
            }
        }
    }
    data class NameParam(@HeaderParam("A simple Header Param") @OpenAPIName("X-NAME") val name: String)

    data class NameResponse(@HeaderParam("A simple Header Param") @OpenAPIName("X-NAME") val name: String)

    data class StringParam(
        @PathParam("A simple String Param") val a: String,
        @QueryParam("Optional String") val optional: String? // Nullable Types are optional
    )

    // A response can be any class, but a description will be generated from the annotation
    @Response("A String Response")
    data class StringResponse(val str: String)
}

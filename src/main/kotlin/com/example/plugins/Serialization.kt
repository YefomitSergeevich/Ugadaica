package com.example.plugins

import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.parameters.HeaderParam
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.route.apiRouting
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlin.random.Random


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
            route("test") {
                get <IntParam, NameResponse>(
                    example = NameResponse("Mister")
                        ) {params ->
                    respond(NameResponse("da"))
                }
            }
            route("inline") {
                get<StringParam, StringResponse>(
                    info(
                        "String Param Endpoint",
                        "This is a String Param Endpoint"
                    ), // A Route module that describes an endpoint, it is optional
                    example = StringResponse("Hi")
                ) { params ->
                    respond(StringResponse(params.a))
                }
            }
            route("rng") {
                get<Unit, RNGParam>(
                    info("Header Param Endpoint", "This is a Header Param Endpoint"),
                    example = RNGParam(54)
                ) {
                    respond(RNGParam())
                }
            }
            route("mini_game") {
                get<GameMiniRequest, GameMiniResponse>(
                    info("Trying to make a game", "Угадайте загаданное число от 1 до 3"),
                ) { params ->
                    respond(GameMiniResponse(null, params.int))
                }
            }
            route("Ugadaica") {
                post<GameRequest, GameResponse, Unit>(
                    info("Угадай число", "Угадай число от 1 до 100 за 7 попыток")
                ) { params, _ ->
                    respond(GameResponse(params.ans))
                }
            }
            route("new_game") {
                post<Unit, GameUpdate, Unit>(
                    info("Начни с чистого листа", "Сбрасывает ходы и загаданное число в игре Ugadaica")
                ) {params, body ->
                    respond(GameUpdate())
                }
            }
            route("choice") {
                post<ChoiceParam, NameResponse, Unit>(
                    info("Позволяет считерить", "Ты можешь сам задать число в игре Ugadaica")
                ) { params, body ->
                    respond(NameResponse("Значение загаданного числа изменено на ${params.int}"))
                }
            }
        }
    }

    var Entries = mutableListOf(
        Entr(
            0,
            "Больше"
        )
    )

    var rightAnswer: Int = Random.nextInt(1, 100)

    data class Entr(@QueryParam("Just RNG") val guess: Int, val answer: String)

    data class GameRequest(@QueryParam("Введите число от 1 до 100") val int: Int) {
        var ans: String?
        var da: Int = int
        private var da2: String = ""
        init {

            if (Entries.count() < 7 && da != rightAnswer) {
                if (da > rightAnswer) {
                    da2 = "Загаданное число меньше чем $da. Осталось ${7 - Entries.count()} попыток"
                } else if (da < rightAnswer) {
                    da2 = "Загаданное число больше чем $da. Осталось ${7 - Entries.count()} попыток"
                }
                else {
                    da2 = "Что то пошло не так. Давай начнем сначала"
                    Entries.clear()
                    rightAnswer = Random.nextInt(1, 100)
                }
            } else if (da == rightAnswer) {
                da2 = "Поздравляю, ты угадал! Правильный ответ был $da"
                Entries.clear()
                rightAnswer = Random.nextInt(1, 100)
            }
            else {
                da2 = "Ты превысил число попыток, давай заново. Правильный ответ был $rightAnswer"
                Entries.clear()
                rightAnswer = Random.nextInt(1, 100)
            }
            val newEntry = Entr(da, da2)
            Entries.add(newEntry)
            ans = da2
        }
    }

    data class GameResponse(@QueryParam("da") val answer: String?)

    data class GameUpdate(@QueryParam("da") var da: String = "Игра обновлена") {

        init {
            Entries.clear()
            rightAnswer = Random.nextInt(1, 100)
        }
    }

    data class GameMiniRequest(@HeaderParam("Введите число от 1 до 3") var int: Int)
    {
        private val rightNum = Random.nextInt(1, 4)
        init {
            if(int == rightNum) {
                int.toString()
                int = 200
            } else {
                int = 400
            }
        }
    }
    data class GameMiniResponse(@HeaderParam("net") var answer: String?, var code: Int)
    {
        init {
            if (code == 200) {
                answer = "Ты угадал"
            }
            else answer = "Ты не угадал"
        }
    }
    data class NameParam(@HeaderParam("A simple Header Param")  val name: String)

    data class IntParam(@HeaderParam("A simple Header Param")  val int: Int)

    data class ChoiceParam(@HeaderParam("A simple Header Param")  val int: Int) {
        init {
            rightAnswer = int
        }
    }

    data class NameResponse(@HeaderParam("A simple Header Param")  val name: String)

    data class RNGParam(@PathParam("Just RNG") val num: Int = Random.nextInt(1, 100))

    data class StringParam(
        @PathParam("A simple String Param") val a: String,
        @QueryParam("Optional String") val optional: String? // Nullable Types are optional
    )

    // A response can be any class, but a description will be generated from the annotation
    @Response("A String Response")
    data class StringResponse(val str: String)

}

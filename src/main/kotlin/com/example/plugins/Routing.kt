package com.example.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.request.*
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.p
import kotlin.random.Random

fun Route.mainRouting() {
    static("/static") {
        resources("files")
    }
    get("/") {
        call.respond(FreeMarkerContent("Main.ftl", mapOf("entries" to Entries)))
    }
}

fun Route.restart() {
    post("/restart") {
        Entries = mutableListOf(
            Entry("Больше", "0")
        )
        call.respondHtml {
            body {
                h1 { +"Печально, правильный ответ был $num" }
                a("/") {
                    +"Попробовать еще раз"
                }
            }
        }
        num = Random.nextInt(1, 100)
        println("Правильный ответ $num")
    }
}

fun Route.choice() {
    get("/choice") {
        call.respond(FreeMarkerContent("Choice.ftl", null))
    }
    post("/choice/change") {
        val params = call.receiveParameters()
        val numNew = params["newNumber"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        num = numNew.toInt()
        call.respondHtml {
            body {
                h1 {
                    +"Ответ заменен на $numNew"
                }
                a("/") {
                    +"Назад"
                }
            }
        }
    }
}

fun Route.status() {
    get("/status") {
        var isGameRunning = false
        if (Entries.count() > 1) isGameRunning = true
        call.respond(mapOf("Is game running" to isGameRunning, "da" to "num"))

    }
}

fun Route.postSubmit() {
    println("Правильный ответ $num")
    post("/submit") {
        val params = call.receiveParameters()
        val body = params["guess"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val headline = if (body.toInt() < num) {
            "Больше"
        } else if (body.toInt() > num) {
            "Меньше"
        } else if (body.toInt() == num) {
            "Поздравляю, вы угадали!!"
        } else "Что то пошло не так"
        val newEntry = Entry(body, headline)
        Entries.add(newEntry)
        if (Entries.count() < 8 && num != body.toInt()) {
            call.respondHtml {
                body {
                    h1 {
                        +"Твой ответ '"
                        +newEntry.numGuess
                        +"' был неверный, попробуй еще"
                    }
                    p {
                        +"Ты уже сделал попыток: ${Entries.count() - 1}"
                    }
                    a("/") {
                        +"Назад"
                    }
                }
            }
        } else if (num == body.toInt()) {
            call.respondHtml {
                body {
                    h1 {
                        +"Победа! Ты написал: "
                        +newEntry.numGuess
                    }
                    p {
                        +"На это ушло попыток: ${Entries.count() - 1}"
                    }
                    a("/") {
                        +"Сыграть еще раз"
                    }
                    Entries = mutableListOf(
                        Entry("Больше", "0")
                    )
                    num = Random.nextInt(1, 100)
                }
            }
            println("Правильный ответ $num")
        } else if (Entries.count() >= 8) {
            call.respondHtml {
                body {
                    h1 {
                        +"Игра окончена"
                    }
                    p {
                        +"Прости, но ты превысил число попыток, правильный ответ был: $num"
                    }
                    a("/") {
                        +"Попытаться еще раз"
                    }
                    Entries = mutableListOf(
                        Entry("Больше", "0")
                    )
                    num = Random.nextInt(1, 100)
                    println("Правильный ответ $num")
                }
            }
        } else {
            call.respondHtml {
                body {
                    h1 {
                        +"ошибка"
                    }
                    p {
                        +"Прости, но что то пошло не так, давай заново, правильный ответ был: $num"
                    }
                    a("/") {
                        +"Попытаться еще раз"
                    }
                    Entries = mutableListOf(
                        Entry("Больше", "0")
                    )
                    num = Random.nextInt(1, 100)
                }
            }
        }
    }
}

fun Application.registerMainRoutes() {
    routing {
        mainRouting()
        postSubmit()
        restart()
        status()
        choice()
    }
}

package com.example.plugins

import kotlin.random.Random

data class Entry(val numGuess: String, val numAnswer: String)

var Entries = mutableListOf(
    Entry(
        "0",
        "Больше"
    )
)
var num = Random.nextInt(1, 100)
package ca.jonathanfritz.ktgame.engine.time

typealias Millis = Long
typealias Seconds = Float

fun Millis.toSeconds(): Seconds = this / 1_000.0f

package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.time

typealias Nanos = Long
typealias Seconds = Float

fun Nanos.toSeconds(): Seconds = this / 1_000_000_000.0f
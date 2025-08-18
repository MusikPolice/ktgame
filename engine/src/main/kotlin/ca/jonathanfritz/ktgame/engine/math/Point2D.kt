package ca.jonathanfritz.ktgame.engine.math

data class Point2D(
    val x: Float,
    val y: Float,
) {
    operator fun minus(other: Point2D): Vector2D = Vector2D(x - other.x, y - other.y)

    operator fun minus(other: Vector2D): Point2D = Point2D(x - other.x, y - other.y)

    operator fun plus(other: Vector2D): Point2D = Point2D(x + other.x, y + other.y)

    companion object {
        fun atOrigin() = Point2D(0f, 0f)
    }
}

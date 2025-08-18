package ca.jonathanfritz.ktgame.engine.math

data class Vector2D(
    val x: Float,
    val y: Float,
) {
    operator fun plus(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)

    operator fun minus(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)

    operator fun minus(other: Float): Vector2D = Vector2D(x - other, y - other)

    operator fun times(scalar: Float): Vector2D = Vector2D(x * scalar, y * scalar)

    operator fun div(scalar: Float): Vector2D = Vector2D(x / scalar, y / scalar)

    val length = kotlin.math.sqrt(x * x + y * y)

    fun normalize(): Vector2D =
        if (length > 0) {
            Vector2D(x / length, y / length)
        } else {
            this
        }

    // sometimes useful to convert a Vector2D to a Point2D, e.g. for rendering
    fun toPoint2D() = Point2D(this.x, this.y)

    companion object {
        fun zero() = Vector2D(0f, 0f)
    }
}

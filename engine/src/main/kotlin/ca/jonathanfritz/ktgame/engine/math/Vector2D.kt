package ca.jonathanfritz.ktgame.engine.math

data class Vector2D (val x: Float, val y: Float) {
    operator fun plus(other: Vector2D): Vector2D {
        return Vector2D(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2D): Vector2D {
        return Vector2D(x - other.x, y - other.y)
    }

    operator fun times(scalar: Float): Vector2D {
        return Vector2D(x * scalar, y * scalar)
    }

    operator fun div(scalar: Float): Vector2D {
        return Vector2D(x / scalar, y / scalar)
    }

    val length = kotlin.math.sqrt(x * x + y * y)

    fun normalize(): Vector2D {
        return if (length > 0) {
            Vector2D(x / length, y / length)
        } else {
            this
        }
    }

    companion object {
        fun zero() = Vector2D(0f, 0f)
    }
}
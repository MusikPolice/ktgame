package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.math

data class Point2D (val x: Float, val y: Float) {

    operator fun minus(other: Point2D): Vector2D {
        return Vector2D(x - other.x, y - other.y)
    }

    operator fun plus(vector2D: Vector2D ): Point2D = Point2D(x + vector2D.x, y + vector2D.y)
}
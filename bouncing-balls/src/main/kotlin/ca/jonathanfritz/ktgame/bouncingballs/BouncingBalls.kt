package ca.jonathanfritz.ktgame.bouncingballs

import ca.jonathanfritz.ktgame.bouncingballs.entities.Ball
import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.Viewport
import ca.jonathanfritz.ktgame.engine.colour.RGBColour
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.math.Vector2D
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.random.Random

private val log = KotlinLogging.logger {}

class BouncingBalls: Scene() {

    private val width = 800
    private val height = 600

    fun run() {
        Viewport(this, "Bouncing Balls", width, height).use {}
    }

    override fun loadResources(nvg: NVG) {
        entities.addAll(
            initializeBalls(100)
        )
    }

    private fun initializeBalls(num: Int): List<Ball> {
        val radius = 5
        val balls = mutableListOf<Ball>()

        do {
            val colour = RGBColour(
                Random.nextInt(0, 255),
                Random.nextInt(0, 255),
                Random.nextInt(0, 255)
            )

            val position = Point2D(
                Random.nextInt(radius, width - radius).toFloat(),
                Random.nextInt(radius, height - radius).toFloat(),
            )

            val minSpeed = -40
            val maxSpeed = 40
            val velocity = Vector2D(
                Random.nextInt(minSpeed, maxSpeed).toFloat(),
                Random.nextInt(minSpeed, maxSpeed).toFloat()
            )

            val ball = Ball.create(radius.toFloat(), colour, position, velocity)
            if (balls.none { it.isCollidingWith(ball) }) {
                balls.add(ball)
            }

        } while(balls.size < num)

        log.debug { "Initialized ${balls.size} balls" }
        return balls
    }

    override fun unloadResources() {
        entities.removeAll { true }
    }
}


fun main() {
    log.debug { "Starting Bouncing Balls..." }
    BouncingBalls().run()
}
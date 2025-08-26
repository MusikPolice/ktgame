package ca.jonathanfritz.ktgame.bouncingballs

import ca.jonathanfritz.ktgame.bouncingballs.entities.Ball
import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.Viewport
import ca.jonathanfritz.ktgame.engine.colour.RGBColour
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingCircleComponent
import ca.jonathanfritz.ktgame.engine.entity.systems.BoundingCircleCollisionSystem
import ca.jonathanfritz.ktgame.engine.entity.systems.PhysicsSystem
import ca.jonathanfritz.ktgame.engine.entity.systems.RenderSystem
import ca.jonathanfritz.ktgame.engine.entity.systems.System
import ca.jonathanfritz.ktgame.engine.entity.systems.ViewportBoundsCollisionSystem
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.math.Vector2D
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.random.Random

private val log = KotlinLogging.logger {}

class BouncingBalls(
    viewport: Viewport,
) : Scene(viewport) {
    override fun loadSystems(nvg: NVG): List<System> =
        listOf(
            ViewportBoundsCollisionSystem(),
            BoundingCircleCollisionSystem(),
            PhysicsSystem(),
            RenderSystem(),
        )

    override fun loadResources(nvg: NVG) {
        entities.addAll(
            initializeBalls(20),
        )
    }

    private fun initializeBalls(num: Int): List<Ball> {
        val radius = 10
        val balls = mutableListOf<Ball>()

        do {
            val colour =
                RGBColour(
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255),
                )

            val position =
                Point2D(
                    Random.nextInt(radius, viewport.width - radius).toFloat(),
                    Random.nextInt(radius, viewport.height - radius).toFloat(),
                )

            val minSpeed = -100
            val maxSpeed = 100
            val velocity =
                Vector2D(
                    Random.nextInt(minSpeed, maxSpeed).toFloat(),
                    Random.nextInt(minSpeed, maxSpeed).toFloat(),
                )

            val ball = Ball.create(radius.toFloat(), colour, position, velocity)
            if (balls.noneCollideWith(ball)) {
                balls.add(ball)
            }
        } while (balls.size < num)

        log.debug { "Initialized ${balls.size} balls" }
        return balls
    }

    // quick and dirty check to ensure that the new ball does not collide with any existing balls
    private fun MutableList<Ball>.noneCollideWith(a: Ball): Boolean =
        this.none { b ->
            val aLoc = a.getComponent(LocationComponent::class)!!
            val bLoc = b.getComponent(LocationComponent::class)!!
            val aCircle = a.getComponent(BoundingCircleComponent::class)!!
            val bCircle = b.getComponent(BoundingCircleComponent::class)!!
            val distance = (aLoc.position - bLoc.position).length
            distance <= (aCircle.radius + bCircle.radius)
        }

    override fun unloadResources() {
        entities.removeAll { true }
    }
}

fun main() {
    log.debug { "Starting Bouncing Balls..." }
    Viewport("Bouncing Balls", 800, 600).use { viewport ->
        BouncingBalls(viewport)
    }
}

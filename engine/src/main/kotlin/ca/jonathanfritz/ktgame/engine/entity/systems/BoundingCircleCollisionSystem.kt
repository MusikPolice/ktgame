package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.PhysicsComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingCircleComponent
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.math.Vector2D
import ca.jonathanfritz.ktgame.engine.time.Millis

/**
 * Checks for and handles collisions between entities.
 * This system only operates on entities that have both a LocationComponent and a BoundingCircleComponent.
 */
class BoundingCircleCollisionSystem(
    val dampingFactor: Float = 0.9f,
) : System {
    override fun update(
        delta: Millis,
        scene: Scene,
    ) {
        val entities =
            scene.entities.filter {
                // TODO: generalize this into an .hasComponents(vararg KClass<Component>) or similar
                it.getComponent(LocationComponent::class) != null &&
                    it.getComponent(BoundingCircleComponent::class) != null &&
                    it.getComponent(PhysicsComponent::class) != null
            }

        for (i in entities.indices) {
            // each entity is checked against all entities to the right of it in the list, avoiding unnecessary
            // double checks of pairs of entities
            for (j in i + 1 until entities.size) {
                val ball1 = entities[i]
                val ball2 = entities[j]

                // draw a vector between ball centres
                val delta = ball2.position() - ball1.position()
                val distance = delta.length
                val overlap = ball1.radius() + ball2.radius() - distance

                // if the balls aren't overlapping, there's no collision to resolve
                if (overlap <= 0) continue

                // normalizing delta gives us the direction of the collision event
                // this also lets us find a perpendicular vector that is tangent to the collision normal
                val normal = delta.normalize()

                // project each ball's velocity onto the normal - essentially its speed in the direction of the collision
                val v1 = ball1.velocity()
                val v2 = ball2.velocity()

                // if the balls are already moving apart, there's no need to continue
                val velocityAlongNormal = (v2 - v1).dot(normal)
                if (velocityAlongNormal > 0) continue

                // compute the impulse to apply to each ball, scaled relative to their masses
                val m1 = ball1.mass()
                val m2 = ball2.mass()

                // impulse magnitude
                val j = ((-1 + dampingFactor) * velocityAlongNormal) / (1 / m1 + 1 / m2)

                // scale the collision normal by that impulse magnitude and add the result to each ball's velocity,
                // scaled inverse to each ball's mass (i.e. the lighter ball is "kicked" more than the heavier ball)
                val impulse = normal * j
                ball1.getComponent(LocationComponent::class)!!.velocity -= impulse * (1 / m1)
                ball2.getComponent(LocationComponent::class)!!.velocity += impulse * (1 / m2)

                // finally, correct each ball's position to prevent them from sticking together when they overlap
                val correctionRatio1 = (1 / m1) / (1 / m1 + 1 / m2)
                val correctionRatio2 = (1 / m2) / (1 / m1 + 1 / m2)

                ball1.getComponent(LocationComponent::class)!!.position = ball1.position() - normal * (overlap * correctionRatio1)
                ball2.getComponent(LocationComponent::class)!!.position = ball2.position() + normal * (overlap * correctionRatio2)
            }
        }
    }

    private fun Entity.position(): Point2D = this.getComponent(LocationComponent::class)!!.position

    private fun Entity.radius(): Float = this.getComponent(BoundingCircleComponent::class)!!.radius

    private fun Entity.velocity(): Vector2D = this.getComponent(LocationComponent::class)!!.velocity

    private fun Entity.mass(): Float = this.getComponent(PhysicsComponent::class)!!.mass
}

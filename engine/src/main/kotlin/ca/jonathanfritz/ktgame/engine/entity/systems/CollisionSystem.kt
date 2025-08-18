package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingCircleComponent
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.math.Vector2D
import ca.jonathanfritz.ktgame.engine.time.Millis

/**
 * Checks for and handles collisions between entities.
 * This system only operates on entities that have both a LocationComponent and a BoundingCircleComponent.
 */
class CollisionSystem : System {
    override fun update(
        delta: Millis,
        scene: Scene,
    ) {
        val entities =
            scene.entities.filter {
                it.getComponent(LocationComponent::class) != null &&
                    it.getComponent(BoundingCircleComponent::class) != null
            }

        for (i in entities.indices) {
            // each entity is checked against all entities to the right of it in the list, avoiding unnecessary
            // double checks of pairs of entities
            for (j in i + 1 until entities.size) {
                val a = entities[i]
                val b = entities[j]
                val aLoc = a.getComponent(LocationComponent::class)!!
                val bLoc = b.getComponent(LocationComponent::class)!!
                val aCircle = a.getComponent(BoundingCircleComponent::class)!!
                val bCircle = b.getComponent(BoundingCircleComponent::class)!!
                val distance = (aLoc.position - bLoc.position).length
                if (distance <= (aCircle.radius + bCircle.radius)) {
                    // entities are colliding!

                    // first try to mitigate overlap by moving them apart so that the entities don't
                    // appear to get stuck on one another (important for fast-moving entities)
                    val overlap = (aCircle.radius + bCircle.radius) - distance
                    val aSpeed = aLoc.velocity.length
                    val bSpeed = bLoc.velocity.length
                    if (aSpeed == 0f && bSpeed == 0f) {
                        // if the entities are stationary, cheat by moving them apart along the line
                        // connecting their centers because neither has a direction
                        val direction = (bLoc.position - aLoc.position).normalize()
                        aLoc.position -= direction * (overlap / 2f)
                        bLoc.position += direction * (overlap / 2f)
                    } else {
                        // if either entity is moving, we can move both backward along their velocity
                        // vector by the ratio of their speeds so that the faster moving entity is
                        // corrected more than the slower moving one
                        val totalSpeed = aSpeed + bSpeed
                        val aShare = if (totalSpeed == 0f) 0.5f else aSpeed / totalSpeed
                        val bShare = if (totalSpeed == 0f) 0.5f else bSpeed / totalSpeed
                        if (aSpeed > 0f) {
                            aLoc.position -= aLoc.velocity.normalize() * (overlap * aShare)
                        }
                        if (bSpeed > 0f) {
                            bLoc.position -= bLoc.velocity.normalize() * (overlap * bShare)
                        }
                    }

                    // now that the entities are no longer overlapping, we can reverse their velocities
                    // and slow them down a little bit to simulate a bounce
                    // TODO: the angle of the collision should be considered, as well as the velocity and
                    //  acceleration of the entities, to make the bounce more realistic
                    aLoc.velocity *= -1f
                    aLoc.acceleration -= 5f
                }
            }
        }
    }
}

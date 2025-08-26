package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingCircleComponent
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

                val collisionVector = aLoc.position - bLoc.position
                val distance = collisionVector.length

                if (distance <= (aCircle.radius + bCircle.radius)) {
                    val aVel = aLoc.velocity
                    val bVel = bLoc.velocity

                    val collisionNormal = collisionVector.normalize()

                    // calculate the magnitude of velocity (i.e. speed) along the collision normal for each ball
                    val aSpeed = Math.abs(aVel.dot(collisionNormal))
                    val bSpeed = Math.abs(bVel.dot(collisionNormal))
                    val totalSpeed = aSpeed + bSpeed

                    val overlap = (aCircle.radius + bCircle.radius) - distance
                    if (totalSpeed > 0f) {
                        // move each ball proportionally to the other's speed
                        // this assumes that the balls have equal mass
                        aLoc.position -= collisionNormal * (overlap * (bSpeed / totalSpeed))
                        bLoc.position += collisionNormal * (overlap * (aSpeed / totalSpeed))
                    } else {
                        // if both balls are completely still, just move them apart equally
                        aLoc.position -= collisionNormal * (overlap / 2f)
                        bLoc.position += collisionNormal * (overlap / 2f)
                    }

                    // project the velocity of each ball onto the collision normal
                    // this gives us the portion of the velocity that is aligned with the collision normal
                    val aVelNormal = collisionNormal * aVel.dot(collisionNormal)
                    val bVelNormal = collisionNormal * bVel.dot(collisionNormal)

                    // the rest of the velocity is perpendicular to the collision normal
                    val aVelTangent = aVel - aVelNormal
                    val bVelTangent = bVel - bVelNormal

                    // we add the two velocities together to get the new velocity after the collision, and apply a
                    // damping factor to simulate energy loss
                    aLoc.velocity = aVelTangent + bVelNormal * dampingFactor
                    bLoc.velocity = bVelTangent + aVelNormal * dampingFactor
                }
            }
        }
    }
}

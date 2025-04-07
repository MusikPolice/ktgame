package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.collision

import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.math.Point2D

class BoundingCircleComponent(
    private val entity: Entity,
    val radius: Float
): BoundingComponent() {

    /**
     * Checks for a collision between this bounding circle and the specified entity
     * If provided, positionOverride will be used as the position of this bounding circle
     */
    override fun isCollidingWith(target: Entity, positionOverride: Point2D?): Boolean {
        target.getBoundingComponent()?.let { targetBoundingComponent ->
            if (targetBoundingComponent is BoundingCircleComponent) {
                val myPosition = positionOverride ?: this.entity.getPosition()
                val targetPosition = target.getPosition()
                if (myPosition != null && targetPosition != null) {
                    val distance = (myPosition - targetPosition).length
                    return distance <= (radius + targetBoundingComponent.radius)
                }
            } else {
                throw UnsupportedOperationException(
                    "Collision detection between ${targetBoundingComponent::class} and BoundingCircleComponent is not supported."
                )
            }
        }

        // if the target entity lacks a bounding component or either entity lacks a position component, they cannot
        // possibly collide with one another
        return false
    }
}
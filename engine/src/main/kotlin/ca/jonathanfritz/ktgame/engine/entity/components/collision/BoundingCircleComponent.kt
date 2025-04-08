package ca.jonathanfritz.ktgame.engine.entity.components.collision

import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.math.Point2D

class BoundingCircleComponent(
    entity: Entity,
    val radius: Float
): BoundingComponent(entity) {

    private val position = (entity.getComponent(LocationComponent::class) as? LocationComponent)?.position
        ?: throw IllegalStateException("BoundingCircleComponent requires a LocationComponent")

    /**
     * Checks for a collision between this bounding circle and the specified entity
     * If provided, positionOverride will be used as the position of this bounding circle
     */
    override fun isCollidingWith(target: Entity, positionOverride: Point2D?): Boolean {
        target.getBoundingComponent()?.let { targetBoundingComponent ->
            if (targetBoundingComponent is BoundingCircleComponent) {
                target.getPosition()?.let { targetPosition ->
                    val distance = (position - targetPosition).length
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

    private fun Entity.getBoundingComponent(): BoundingComponent? {
        return this.getComponent(BoundingComponent::class) as? BoundingComponent
    }

    private fun Entity.getPosition(): Point2D? {
        return (this.getComponent(LocationComponent::class) as? LocationComponent)?.position
    }
}
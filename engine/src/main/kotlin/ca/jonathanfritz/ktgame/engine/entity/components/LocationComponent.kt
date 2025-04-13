package ca.jonathanfritz.ktgame.engine.entity.components

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.math.Vector2D
import ca.jonathanfritz.ktgame.engine.time.Nanos
import ca.jonathanfritz.ktgame.engine.time.toSeconds

/**
 * Represents a component that contains location and movement information for an entity.
 *
 * @property position The current position of the entity in 2D space
 * @property velocity The current velocity of the entity in units per second
 * @property acceleration The current acceleration of the entity in units per second squared
 */
data class LocationComponent(
    val entity: Entity,
    var position: Point2D = Point2D(0f, 0f),
    var velocity: Vector2D = Vector2D(0f, 0f),
    var acceleration: Vector2D = Vector2D(0f, 0f),
) : Component(entity) {
    override fun update(
        delta: Nanos,
        scene: Scene,
    ) {
        // apply physics to move the entity along its trajectory
        // TODO: what about gravity and/or friction? other forces?
        val (newVelocity, newPosition) = calculateAcceleration(delta)
        if (newPosition == position) {
            // no movement, so nothing to do
            return
        }

        // check to see if any collisions would occur if we moved the entity to the newly calculated position
        val collisions = entity.isColliding(scene, newPosition)
        if (collisions.isEmpty()) {
            // best case scenario - no collisions, so we can go ahead and move the entity
            velocity = newVelocity
            position = newPosition
        } else {
            var prevStepCollisions = collisions

            // walk delta back to find the position of the entity just before the collision
            // TODO: a binary search would likely be more efficient here
            for (midDelta in delta - 1 downTo 0) {
                val (midVelocity, midPosition) = calculateAcceleration(midDelta)

                // check to see if any collisions would occur if we moved the entity to the newly calculated position
                val midCollisions = entity.isColliding(scene, midPosition)
                if (collisions.isEmpty()) {
                    // the entity will collide with entities in prevStepCollisions at midDelta + 1
                    velocity = midVelocity
                    position = midPosition
                    entity.handleCollision(prevStepCollisions)

                    // collision handled, we're done here
                    return
                } else {
                    // record any collisions that occur at this step
                    // this will give us a list of collisions that would occur if time moved forward by 1 nanosecond
                    prevStepCollisions = midCollisions
                }
            }
        }
    }

    private fun calculateAcceleration(delta: Nanos): Pair<Vector2D, Point2D> {
        val newVelocity = velocity + (acceleration * delta.toSeconds())
        val newPosition = position + (newVelocity * delta.toSeconds())
        return Pair(newVelocity, newPosition)
    }

    /**
     * Returns the list of entities that are colliding with the receiver entity
     * If provided, positionOverride will be used as the position of the receiver entity
     */
    private fun Entity.isColliding(
        scene: Scene,
        positionOverride: Point2D? = null,
    ): List<Entity> =
        scene.entities
            .filterNot { target ->
                // don't check for collision with self
                target == this
            }.filter { target ->
                this.isCollidingWith(target, positionOverride)
            }
}

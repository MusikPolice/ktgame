package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine

import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.Entity

/**
 * A scene is a container for the world, all entities that inhabit it, and the game logic that drives it
 */
abstract class Scene(val entities: MutableList<Entity>) {

    // TODO: rather than being open, it might make sense to allow event handlers to be specified?
    open fun mainLoop() {
        var lastTickNanos = System.nanoTime()

        while (true) {
            // TODO: rendering stuff

            val now = System.nanoTime()
            entities.forEach { entity ->
                entity.update(now - lastTickNanos, this)
            }
            lastTickNanos = now
        }
    }

    // when called, entity is not colliding with targets, but if it stepped forward one more nano, it would be
    // goal here is to resolve the collision by modifying each entity's position and velocity such that they start
    // moving away from each other
    open fun handleCollision(entity: Entity, targets: List<Entity>) {
        // TODO: resolve collisions between entity and any targets
        //       does this belong here? should it be in the entity instead?
    }
}
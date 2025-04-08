package ca.jonathanfritz.ktgame.engine

import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.time.Nanos

/**
 * A scene is a container for the world, all entities that inhabit it, and the game logic that drives it
 */
abstract class Scene {

    val entities: MutableList<Entity> = mutableListOf()

    /**
     * Loads any resources that the scene or its entities require, initializes entities
     */
    abstract fun loadResources(nvg: NVG)

    /**
     * Draws all entities to the nvg context in no particular order
     */
    open fun render(nvg: NVG) {
        // TODO: sort entities by z-index or some other criteria
        entities.forEach { it.render(nvg) }
    }

    /**
     * Updates each entity, advancing the game state by delta nanoseconds
     */
    open fun update(delta: Nanos) {
        entities.forEach { it.update(delta, this) }
    }

    /**
     * Unloads any resources that the scene or its entities require, cleans up entities
     */
    abstract fun unloadResources()

    // when called, entity is not colliding with targets, but if it stepped forward one more nano, it would be
    // goal here is to resolve the collision by modifying each entity's position and velocity such that they start
    // moving away from each other
    open fun handleCollision(entity: Entity, targets: List<Entity>) {
        // TODO: resolve collisions between entity and any targets
        //       does this belong here? should it be in the entity instead?
        targets.plus(entity).mapNotNull{
            it.getComponent(LocationComponent::class) as? LocationComponent
        }.forEach { location ->
            // as a temporary solve, just reverse the velocity of each entity
            // TODO: consider the direction of the collision, mass of each entity, etc
            location.velocity *= -1f
        }
    }
}
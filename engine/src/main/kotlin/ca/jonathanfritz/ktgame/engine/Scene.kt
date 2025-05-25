package ca.jonathanfritz.ktgame.engine

import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.time.Millis

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
    open fun update(delta: Millis) {
        entities.forEach { it.update(delta, this) }
    }

    /**
     * Unloads any resources that the scene or its entities require, cleans up entities
     */
    abstract fun unloadResources()
}

package ca.jonathanfritz.ktgame.engine

import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.entity.systems.System
import ca.jonathanfritz.ktgame.engine.time.Millis
import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

/**
 * A scene is a container for the world, all entities that inhabit it, and the game logic that drives it
 */
abstract class Scene {
    val viewport: Viewport
    val entities: MutableList<Entity> = mutableListOf()
    private var systems: List<System>

    constructor(viewport: Viewport) {
        try {
            this.viewport = viewport

            log.debug { "Loading Systems..." }
            systems = loadSystems(viewport.nvg)
            log.debug { "Loaded Systems" }

            log.debug { "Loading Scene Resources..." }
            loadResources(viewport.nvg)
            log.debug { "Loaded Scene Resources" }

            // everything is loaded, loop until the window is closed
            viewport.mainLoop(this)
        } finally {
            // always clean up scene resources
            log.debug { "Unloading Scene Resources..." }
            unloadResources()
            log.debug { "Unloaded Scene Resources" }
        }
    }

    abstract fun loadSystems(nvg: NVG): List<System>

    /**
     * Loads any resources that the scene or its entities require, initializes entities
     */
    abstract fun loadResources(nvg: NVG)

    /**
     * Draws all entities to the nvg context in no particular order
     */
    open fun render(nvg: NVG) {
        systems.forEach { it.render(this) }
    }

    /**
     * Updates each system, advancing the game state by delta nanoseconds
     */
    open fun update(delta: Millis) {
        systems.forEach { it.update(delta, this) }
    }

    /**
     * Unloads any resources that the scene or its entities require, cleans up entities
     */
    abstract fun unloadResources()
}

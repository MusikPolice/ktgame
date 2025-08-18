package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.time.Millis

/**
 * A System is responsible for updating and/or rendering a specific aspect of the game.
 * Systems typically operate on entities that have a specific set of components.
 */
interface System {
    fun update(
        delta: Millis,
        scene: Scene,
    )

    fun render(scene: Scene) {}
}

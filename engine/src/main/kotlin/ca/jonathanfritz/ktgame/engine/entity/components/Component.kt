package ca.jonathanfritz.ktgame.engine.entity.components

import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.time.Millis

abstract class Component(
    entity: Entity,
) {
    open fun update(
        delta: Millis,
        scene: Scene,
    ) {
        // Default implementation does nothing
    }

    open fun render(nvg: NVG) {
        // Default implementation does nothing
    }
}

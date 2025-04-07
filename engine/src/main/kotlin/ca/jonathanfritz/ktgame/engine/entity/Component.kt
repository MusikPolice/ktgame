package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity

import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.time.Nanos

abstract class Component {

    open fun update(delta: Nanos, scene: Scene) {
        // Default implementation does nothing
    }

    // TODO: this needs to accept some kind of rendering context
    open fun render() {
        // Default implementation does nothing
    }
}
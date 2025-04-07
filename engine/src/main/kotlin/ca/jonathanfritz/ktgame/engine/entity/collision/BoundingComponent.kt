package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.collision

import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.Component
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.math.Point2D

abstract class BoundingComponent: Component() {

    open fun isCollidingWith(target: Entity, positionOverride: Point2D? = null): Boolean {
        // default implementation does nothing
        return false
    }
}
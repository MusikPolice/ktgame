package ca.jonathanfritz.ktgame.engine.entity.components.collision

import ca.jonathanfritz.ktgame.engine.entity.components.Component
import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.math.Point2D

abstract class BoundingComponent(entity: Entity): Component(entity) {

    open fun isCollidingWith(target: Entity, positionOverride: Point2D? = null): Boolean {
        // default implementation does nothing
        return false
    }
}
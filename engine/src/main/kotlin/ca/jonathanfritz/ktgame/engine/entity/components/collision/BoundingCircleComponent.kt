package ca.jonathanfritz.ktgame.engine.entity.components.collision

import ca.jonathanfritz.ktgame.engine.entity.Entity

class BoundingCircleComponent(
    entity: Entity,
    val radius: Float,
) : BoundingComponent(entity)

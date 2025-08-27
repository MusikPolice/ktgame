package ca.jonathanfritz.ktgame.engine.entity.components

import ca.jonathanfritz.ktgame.engine.entity.Entity

class PhysicsComponent(
    entity: Entity,
    val mass: Float = 1f,
) : Component(entity)

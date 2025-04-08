package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.components

import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingComponent

abstract class NanoVGRenderComponent(entity: Entity): Component(entity) {

    val location = entity.getComponent(LocationComponent::class) as? LocationComponent
        ?: throw IllegalStateException("NanoVGRenderComponent requires a LocationComponent")

    val bounding = entity.getComponent(BoundingComponent::class) as? BoundingComponent
        ?: throw IllegalStateException("NanoVGRenderComponent requires a BoundingComponent")

    override fun render(nvg: NVG) {
        // Default implementation does nothing
    }
}
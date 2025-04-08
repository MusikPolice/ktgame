package ca.jonathanfritz.ktgame.engine.entity.components

import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingComponent

abstract class NanoVGRenderComponent(entity: Entity): Component(entity) {

    private val location = entity.getComponent(LocationComponent::class) as? LocationComponent
        ?: throw IllegalStateException("NanoVGRenderComponent requires a LocationComponent")

    private val bounding = entity.getComponent(BoundingComponent::class) as? BoundingComponent
        ?: throw IllegalStateException("NanoVGRenderComponent requires a BoundingComponent")

    override fun render(nvg: NVG) {
        // Default implementation does nothing
    }
}
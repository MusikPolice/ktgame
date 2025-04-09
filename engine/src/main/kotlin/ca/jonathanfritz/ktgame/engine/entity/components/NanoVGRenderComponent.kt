package ca.jonathanfritz.ktgame.engine.entity.components

import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.colour.RGBColour
import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingComponent

abstract class NanoVGRenderComponent(entity: Entity, colour: RGBColour): Component(entity) {

    protected val location: LocationComponent by lazy {
        entity.getComponent(LocationComponent::class)
            ?: throw IllegalStateException("NanoVGRenderComponent requires a LocationComponent")
    }

    protected val bounding: BoundingComponent by lazy {
        entity.getComponent(BoundingComponent::class)
            ?: throw IllegalStateException("NanoVGRenderComponent requires a BoundingComponent")
    }

    abstract override fun render(nvg: NVG)
}
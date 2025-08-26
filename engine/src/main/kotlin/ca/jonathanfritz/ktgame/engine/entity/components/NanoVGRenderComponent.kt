package ca.jonathanfritz.ktgame.engine.entity.components

import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.entity.Entity

abstract class NanoVGRenderComponent(
    entity: Entity,
) : Component(entity) {
    // this method is meant to be implemented by each Entity that should be rendered to the NanoVG context,
    // allowing each entity to be drawn differently
    abstract fun render(
        nvg: NVG,
        locationComponent: LocationComponent,
    )
}

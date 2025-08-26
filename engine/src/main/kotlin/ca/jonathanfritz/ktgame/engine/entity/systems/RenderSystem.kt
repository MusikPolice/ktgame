package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.NanoVGRenderComponent
import ca.jonathanfritz.ktgame.engine.time.Millis

class RenderSystem : System {
    override fun update(
        delta: Millis,
        scene: Scene,
    ) { }

    override fun render(scene: Scene) {
        // only entities with both a LocationComponent and NanoVGRenderComponent can be rendered
        scene.entities
            .filter {
                it.getComponent(LocationComponent::class) != null &&
                    it.getComponent(NanoVGRenderComponent::class) != null
            }.forEach { entity ->
                val renderComponent = entity.getComponent(NanoVGRenderComponent::class)!!
                val locationComponent = entity.getComponent(LocationComponent::class)!!
                renderComponent.render(scene.viewport.nvg, locationComponent)
            }
    }
}

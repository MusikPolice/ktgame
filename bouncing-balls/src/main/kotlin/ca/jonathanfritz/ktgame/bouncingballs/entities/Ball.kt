package ca.jonathanfritz.ktgame.bouncingballs.entities

import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.colour.RGBColour
import ca.jonathanfritz.ktgame.engine.entity.Entity
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.NanoVGRenderComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingCircleComponent
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.math.Vector2D
import org.lwjgl.nanovg.NanoVG

class Ball private constructor() : Entity() {
    companion object {
        /**
         * Factory method for creating an instance of Ball with one or more components
         */
        fun create(
            radius: Float,
            colour: RGBColour,
            position: Point2D = Point2D.atOrigin(),
            velocity: Vector2D = Vector2D.zero(),
            acceleration: Vector2D = Vector2D.zero(),
        ): Ball =
            create(
                { Ball() },
                { entity -> LocationComponent(entity, position, velocity, acceleration) },
                { entity -> BoundingCircleComponent(entity, radius) },
                { entity ->
                    object : NanoVGRenderComponent(entity, colour) {
                        override fun render(
                            nvg: NVG,
                            locationComponent: LocationComponent,
                        ) {
                            NanoVG.nvgBeginPath(nvg)
                            NanoVG.nvgCircle(nvg, locationComponent.position.x, locationComponent.position.y, radius)
                            NanoVG.nvgFillColor(nvg, colour.nvgColor)
                            NanoVG.nvgFill(nvg)
                        }
                    }
                },
            )
    }
}

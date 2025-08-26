package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingCircleComponent
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.time.Millis

class ViewportBoundsCollisionSystem(
    val dampingFactor: Float = 0.9f,
) : System {
    override fun update(
        delta: Millis,
        scene: Scene,
    ) {
        scene.entities
            .filter {
                it.getComponent(LocationComponent::class) != null &&
                    it.getComponent(BoundingCircleComponent::class) != null
            }.forEach {
                // TODO: this should allow each wall to be turned on or off so that some scenes could be missing a bottom wall, etc.
                val loc = it.getComponent(LocationComponent::class)!!
                val circle = it.getComponent(BoundingCircleComponent::class)!!

                if (loc.position.x - circle.radius < 0) {
                    // left wall
                    loc.position = loc.position.copy(x = circle.radius)
                    loc.velocity =
                        loc.velocity.copy(
                            x = -loc.velocity.x * dampingFactor,
                            y = loc.velocity.y * dampingFactor,
                        )
                } else if (loc.position.x + circle.radius > scene.viewport.width) {
                    // right wall
                    loc.position = loc.position.copy(x = scene.viewport.width - circle.radius)
                    loc.velocity =
                        loc.velocity.copy(
                            x = -loc.velocity.x * dampingFactor,
                            y = loc.velocity.y * dampingFactor,
                        )
                }

                if (loc.position.y - circle.radius < 0) {
                    // top wall
                    loc.position = loc.position.copy(y = circle.radius)
                    loc.velocity =
                        loc.velocity.copy(
                            x = loc.velocity.x * dampingFactor,
                            y = -loc.velocity.y * dampingFactor,
                        )
                } else if (loc.position.y + circle.radius > scene.viewport.height) {
                    // bottom wall
                    loc.position = loc.position.copy(y = scene.viewport.height - circle.radius)
                    loc.velocity =
                        loc.velocity.copy(
                            x = loc.velocity.x * dampingFactor,
                            y = -loc.velocity.y * dampingFactor,
                        )
                }
            }
    }
}

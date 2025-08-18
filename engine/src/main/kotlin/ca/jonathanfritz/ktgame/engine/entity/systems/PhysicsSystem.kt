package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.time.Millis

/**
 * A simple physics system that updates the position of entities based on their velocity and acceleration.
 * This system only operates on entities that have a LocationComponent.
 */
class PhysicsSystem : System {
    override fun update(
        delta: Millis,
        scene: Scene,
    ) {
        scene.entities.forEach { entity ->
            entity.getComponent(LocationComponent::class)?.let { location ->
                // Simple Euler integration
                location.velocity += location.acceleration * (delta / 1000f)
                location.position += location.velocity * (delta / 1000f)
            }
        }
    }
}

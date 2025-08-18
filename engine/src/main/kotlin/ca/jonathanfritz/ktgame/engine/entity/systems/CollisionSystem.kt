package ca.jonathanfritz.ktgame.engine.entity.systems

import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingCircleComponent
import ca.jonathanfritz.ktgame.engine.time.Millis

/**
 * Checks for and handles collisions between entities.
 * This system only operates on entities that have both a LocationComponent and a BoundingCircleComponent.
 */
class CollisionSystem : System {
    override fun update(
        delta: Millis,
        scene: Scene,
    ) {
        val entities =
            scene.entities.filter {
                it.getComponent(LocationComponent::class) != null &&
                    it.getComponent(BoundingCircleComponent::class) != null
            }

        // each entity is checked against all entities to the right of it in the list, avoiding unnecessary
        // double checks of pairs of entities
        for (i in entities.indices) {
            for (j in i + 1 until entities.size) {
                val a = entities[i]
                val b = entities[j]
                val aLoc = a.getComponent(LocationComponent::class)!!
                val bLoc = b.getComponent(LocationComponent::class)!!
                val aCircle = a.getComponent(BoundingCircleComponent::class)!!
                val bCircle = b.getComponent(BoundingCircleComponent::class)!!
                val distance = (aLoc.position - bLoc.position).length
                if (distance <= (aCircle.radius + bCircle.radius)) {
                    // TODO: Handle collision (for now, just print or mark)
                    println("Collision detected between $a and $b")
                }
            }
        }
    }
}

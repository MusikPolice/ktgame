package ca.jonathanfritz.ktgame.engine.entity

import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingComponent
import ca.jonathanfritz.ktgame.engine.entity.components.Component
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.time.Nanos
import kotlin.reflect.KClass

data class Entity (
    val components: List<Component>
) {
    // internally, components are expressed in a map for fast lookup
    // this does mean that each Entity can have at most one component of each type
    private val componentMap: Map<KClass<out Component>, Component> = components.associateBy { it::class }

    private val boundingComponent = getComponent(BoundingComponent::class) as? BoundingComponent

    fun update(delta: Nanos, scene: Scene) {
        // TODO: does the order of components matter? maybe we need a priority system?
        componentMap.values.forEach { it.update(delta, scene) }
    }

    fun render(nvg: NVG) {
        // TODO: does the rendering order matter? maybe we need a priority system?
        // TODO: what about debug rendering? we might want a way to toggle rendering for some components
        componentMap.values.forEach { it.render(nvg) }
    }

    /**
     * Returns the component of the specified type, or null if it does not exist
     */
    fun getComponent(kClass: KClass<out Component>): Component? =
        componentMap.getOrDefault(kClass, null)

    /**
     * Returns true if this entity is colliding with the target entity
     * If specified, positionOverride will be used as the position of this entity
     */
    fun isCollidingWith(target: Entity, positionOverride: Point2D? = null): Boolean {
        return boundingComponent?.isCollidingWith(target, positionOverride) ?: false
    }
}
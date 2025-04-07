package ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity

import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.entity.collision.BoundingComponent
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.ca.jonathanfritz.ktgame.engine.time.Nanos
import kotlin.reflect.KClass

data class Entity (
    val components: List<Component>
) {
    // internally, components are expressed in a map for fast lookup
    // this does mean that each Entity can have at most one component of each type
    private val componentMap: Map<KClass<out Component>, Component> = components.associateBy { it::class }
    fun getComponent(kClass: KClass<out Component>): Component? =
        componentMap.getOrDefault(kClass, null)

    fun update(delta: Nanos, scene: Scene) {
        // TODO: does the order of components matter? maybe we need a priority system?
        componentMap.values.forEach { it.update(delta, scene) }
    }

    fun getPosition(): Point2D? {
        return (this.getComponent(LocationComponent::class) as? LocationComponent)?.position
    }

    fun getBoundingComponent(): BoundingComponent? {
        return this.getComponent(BoundingComponent::class) as? BoundingComponent
    }

    /**
     * Returns true if this entity is colliding with the target entity
     * If specified, positionOverride will be used as the position of this entity
     */
    fun isCollidingWith(target: Entity, positionOverride: Point2D? = null): Boolean {
        return this.getBoundingComponent()?.isCollidingWith(target, positionOverride) ?: false
    }
}
package ca.jonathanfritz.ktgame.engine.entity

import ca.jonathanfritz.ktgame.engine.NVG
import ca.jonathanfritz.ktgame.engine.Scene
import ca.jonathanfritz.ktgame.engine.entity.components.Component
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
import ca.jonathanfritz.ktgame.engine.entity.components.collision.BoundingComponent
import ca.jonathanfritz.ktgame.engine.math.Point2D
import ca.jonathanfritz.ktgame.engine.time.Millis
import kotlin.reflect.KClass

abstract class Entity {
    // internally, components are expressed in a map for fast lookup
    // this does mean that each Entity can have at most one component of each type
    val componentMap: MutableMap<KClass<out Component>, Component> = mutableMapOf()

    companion object {
        /**
         * Helper function that creates an instance of a subtype of Entity that has one or more components
         */
        fun <T : Entity> create(
            constructor: () -> T,
            vararg components: (Entity) -> Component,
        ): T {
            val entity = constructor()
            components.forEach { component ->
                entity.addComponent(component(entity))
            }
            return entity
        }
    }

    fun addComponent(component: Component): Entity {
        componentMap[component::class] = component
        return this
    }

    inline fun <reified T : Component> getComponent(kClass: KClass<out T>): T? {
        // in the ideal case, the specified kClass is a key in the map
        return componentMap.getOrElse(kClass) {
            // but it could be a subclass of one of the keys in the map
            // in that case, we need to check each entry in the map
            componentMap.values
                .firstOrNull { instance ->
                    kClass.isInstance(instance)
                }?.also {
                    componentMap[kClass] = (it as T)
                }
        } as? T
    }

    fun update(
        delta: Millis,
        scene: Scene,
    ) {
        // TODO: does the order of components matter? maybe we need a priority system?
        componentMap.values.forEach { it.update(delta, scene) }
    }

    fun render(nvg: NVG) {
        // TODO: does the rendering order matter? maybe we need a priority system?
        // TODO: what about debug rendering? we might want a way to toggle rendering for some components
        componentMap.values.forEach { it.render(nvg) }
    }

    /**
     * Returns true if this entity is colliding with the target entity
     * If specified, positionOverride will be used as the position of this entity
     */
    fun isCollidingWith(
        target: Entity,
        positionOverride: Point2D? = null,
    ): Boolean {
        val boundingComponent: BoundingComponent =
            getComponent(BoundingComponent::class)
                ?: throw IllegalStateException("Entity requires a BoundingComponent")

        return boundingComponent.isCollidingWith(target, positionOverride)
    }

    // when called, entity is not colliding with targets, but if it stepped forward one more nano, it would be
    // goal here is to resolve the collision by modifying each entity's position and velocity such that they start
    // moving away from each other
    open fun handleCollision(targets: List<Entity>) {
        targets
            .plus(this)
            .mapNotNull {
                it.getComponent(LocationComponent::class)
            }.forEach { location ->
                // as a temporary solve, just reverse the velocity of each entity
                // TODO: consider the direction of the collision, mass of each entity, etc
                location.velocity *= -1f
            }
    }
}

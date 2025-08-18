package ca.jonathanfritz.ktgame.engine.entity

import ca.jonathanfritz.ktgame.engine.entity.components.Component
import ca.jonathanfritz.ktgame.engine.entity.components.LocationComponent
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
}

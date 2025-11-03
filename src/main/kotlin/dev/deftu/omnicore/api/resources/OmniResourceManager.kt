@file:JvmName("OmniResourceManager")

package dev.deftu.omnicore.api.resources

import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.resources.ResourceLocation
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

public fun ResourceManager.findFirst(location: ResourceLocation): Optional<Resource> {
    //#if MC >= 1.19.2
    return getResource(location)
    //#else
    //$$ return try {
    //$$     return Optional.ofNullable(getResource(location))
    //$$ } catch (e: Exception) {
    //$$     Optional.empty()
    //$$ }
    //#endif
}

public fun ResourceManager.findFirstOrThrow(location: ResourceLocation): Resource {
    //#if MC >= 1.19.2
    return getResourceOrThrow(location)
    //#else
    //$$ return getResource(location) ?: throw IllegalArgumentException("Resource not found: location")
    //#endif
}

public fun ResourceManager.findFirstOrNull(location: ResourceLocation): Resource? {
    //#if MC >= 1.19.2
    return getResource(location).getOrNull()
    //#else
    //$$ return try {
    //$$     getResource(location)
    //$$ } catch (e: Exception) {
    //$$     null
    //$$ }
    //#endif
}

public fun ResourceManager.findAll(location: ResourceLocation): List<Resource> {
    return getResourceStack(location).toList()
}

public fun ResourceManager.location(location: ResourceLocation): Sequence<Resource> {
    return getResourceStack(location).asSequence()
}

public fun ResourceManager.exists(location: ResourceLocation): Boolean {
    return findFirst(location).isPresent
}

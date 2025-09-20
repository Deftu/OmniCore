@file:JvmName("OmniResourceManager")

package dev.deftu.omnicore.api.resources

import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

public fun ResourceManager.findFirst(location: Identifier): Optional<Resource> {
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

public fun ResourceManager.findFirstOrThrow(location: Identifier): Resource {
    //#if MC >= 1.19.2
    return getResourceOrThrow(location)
    //#else
    //$$ return getResource(location) ?: throw IllegalArgumentException("Resource not found: location")
    //#endif
}

public fun ResourceManager.findFirstOrNull(location: Identifier): Resource? {
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

public fun ResourceManager.findAll(location: Identifier): List<Resource> {
    return getAllResources(location).toList()
}

public fun ResourceManager.location(location: Identifier): Sequence<Resource> {
    return getAllResources(location).asSequence()
}

public fun ResourceManager.exists(location: Identifier): Boolean {
    return findFirst(location).isPresent
}

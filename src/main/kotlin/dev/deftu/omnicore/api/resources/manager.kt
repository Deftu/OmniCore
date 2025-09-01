@file:JvmName("OmniResourceManager")

package dev.deftu.omnicore.api.resources

import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

public fun ResourceManager.findFirst(identifier: Identifier): Optional<Resource> {
    //#if MC >= 1.19.2
    return getResource(identifier)
    //#else
    //$$ return try {
    //$$     return Optional.ofNullable(getResource(identifier))
    //$$ } catch (e: Exception) {
    //$$     Optional.empty()
    //$$ }
    //#endif
}

public fun ResourceManager.findFirstOrThrow(identifier: Identifier): Resource {
    //#if MC >= 1.19.2
    return getResourceOrThrow(identifier)
    //#else
    //$$ return getResource(identifier) ?: throw IllegalArgumentException("Resource not found: $identifier")
    //#endif
}

public fun ResourceManager.findFirstOrNull(identifier: Identifier): Resource? {
    //#if MC >= 1.19.2
    return getResource(identifier).getOrNull()
    //#else
    //$$ return try {
    //$$     getResource(identifier)
    //$$ } catch (e: Exception) {
    //$$     null
    //$$ }
    //#endif
}

public fun ResourceManager.findAll(identifier: Identifier): List<Resource> {
    return getAllResources(identifier).toList()
}

public fun ResourceManager.findSequence(identifier: Identifier): Sequence<Resource> {
    return getAllResources(identifier).asSequence()
}

public fun ResourceManager.exists(identifier: Identifier): Boolean {
    return findFirst(identifier).isPresent
}

package dev.deftu.omnicore.api.resources

import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.util.Optional

//#if MC >= 1.19.2
import kotlin.jvm.optionals.getOrNull
//#endif

public object OmniResourceManager {
    @JvmStatic
    public fun findFirst(
        resourceManager: ResourceManager,
        identifier: Identifier
    ): Optional<Resource> {
        //#if MC >= 1.19.2
        return resourceManager.getResource(identifier)
        //#else
        //$$ return try {
        //$$     return Optional.ofNullable(resourceManager.getResource(identifier))
        //$$ } catch (e: Exception) {
        //$$     Optional.empty()
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun findFirstOrThrow(
        resourceManager: ResourceManager,
        identifier: Identifier
    ): Resource {
        //#if MC >= 1.19.2
        return resourceManager.getResourceOrThrow(identifier)
        //#else
        //$$ return resourceManager.getResource(identifier) ?: throw IllegalArgumentException("Resource not found: $identifier")
        //#endif
    }

    @JvmStatic
    public fun findFirstOrNull(
        resourceManager: ResourceManager,
        identifier: Identifier
    ): Resource? {
        //#if MC >= 1.19.2
        return resourceManager.getResource(identifier).getOrNull()
        //#else
        //$$ return try {
        //$$     resourceManager.getResource(identifier)
        //$$ } catch (e: Exception) {
        //$$     null
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun findAll(
        resourceManager: ResourceManager,
        identifier: Identifier
    ): List<Resource> {
        return resourceManager.getAllResources(identifier).toList()
    }

    @JvmStatic
    public fun findSequence(
        resourceManager: ResourceManager,
        identifier: Identifier
    ): Sequence<Resource> {
        return resourceManager.getAllResources(identifier).asSequence()
    }

    @JvmStatic
    public fun exists(
        resourceManager: ResourceManager,
        identifier: Identifier
    ): Boolean {
        return findFirst(resourceManager, identifier).isPresent
    }
}

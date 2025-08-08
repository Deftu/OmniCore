package dev.deftu.omnicore.common.resources

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.util.Optional

public fun ResourceManager.findFirst(identifier: Identifier): Optional<Resource> {
    return OmniResourceManager.findFirst(this, identifier)
}

public fun ResourceManager.findFirstOrThrow(identifier: Identifier): Resource {
    return OmniResourceManager.findFirstOrThrow(this, identifier)
}

public fun ResourceManager.findAll(identifier: Identifier): List<Resource> {
    return OmniResourceManager.findAll(this, identifier)
}

public object OmniResourceManager {

    @JvmStatic
    @GameSide(Side.BOTH)
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
    @GameSide(Side.BOTH)
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
    @GameSide(Side.BOTH)
    public fun findAll(
        resourceManager: ResourceManager,
        identifier: Identifier
    ): List<Resource> {
        return resourceManager.getAllResources(identifier).toList()
    }

}

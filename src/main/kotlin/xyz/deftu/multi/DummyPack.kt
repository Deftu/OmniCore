//#if MC >= 1.19.3
package xyz.deftu.multi

import net.minecraft.resource.ResourcePack
import net.minecraft.resource.ResourceType
import net.minecraft.resource.metadata.ResourceMetadataReader
import net.minecraft.util.Identifier

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
internal object DummyResourcePack : ResourcePack {
    override fun getName() = "__generated__"
    override fun close() = throw UnsupportedOperationException()
    override fun openRoot(vararg strings: String?) = throw UnsupportedOperationException()
    override fun open(resourceType: ResourceType?, identifier: Identifier?) = throw UnsupportedOperationException()
    override fun findResources(
        resourceType: ResourceType?,
        string: String?,
        string2: String?,
        resultConsumer: ResourcePack.ResultConsumer?
    ) = throw UnsupportedOperationException()
    override fun getNamespaces(resourceType: ResourceType?) = throw UnsupportedOperationException()
    override fun <T : Any?> parseMetadata(resourceMetadataReader: ResourceMetadataReader<T>?) = throw UnsupportedOperationException()
}

//#endif

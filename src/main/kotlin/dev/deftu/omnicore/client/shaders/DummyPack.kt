//#if MC >= 1.19.3
package dev.deftu.omnicore.client.shaders

//#if MC >= 1.20.5
//$$ import net.minecraft.resource.ResourcePackInfo
//#endif

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
    //#if FABRIC
    override fun openRoot(vararg strings: String?) = throw UnsupportedOperationException()
    override fun open(resourceType: ResourceType?, identifier: Identifier?) = throw UnsupportedOperationException()
    override fun findResources(
        resourceType: ResourceType?,
        string: String?,
        string2: String?,
        resultConsumer: ResourcePack.ResultConsumer?
    ) = throw UnsupportedOperationException()
    override fun <T : Any?> parseMetadata(resourceMetadataReader: ResourceMetadataReader<T>?) = throw UnsupportedOperationException()
    //#else
    //$$ override fun getRootResource(vararg strings: String?) = throw UnsupportedOperationException()
    //$$ override fun getResource(packType: PackType?, resourceLocation: ResourceLocation?) = throw UnsupportedOperationException()
    //$$ override fun listResources(
    //$$     packType: PackType?,
    //$$     string: String?,
    //$$     string2: String?,
    //$$     resultConsumer: PackResources.ResourceOutput?
    //$$ ) = throw UnsupportedOperationException()
    //$$ override fun <T : Any?> getMetadataSection(arg: MetadataSectionSerializer<T>) = throw UnsupportedOperationException()
    //#endif
    override fun getNamespaces(resourceType: ResourceType?) = throw UnsupportedOperationException()
    //#if MC >= 1.20.5
    //$$ override fun getInfo(): ResourcePackInfo = throw UnsupportedOperationException()
    //#endif
}
//#endif

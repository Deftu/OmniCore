package dev.deftu.omnicore.internal.client.render.shader

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.metadata.MetadataSectionSerializer

//#if MC >= 1.20.5
import net.minecraft.server.packs.PackLocationInfo
//#endif

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
internal object GeneratedResourcePack : PackResources {
    override fun packId() = "__generated__"
    override fun close() = throw UnsupportedOperationException()

    override fun getRootResource(vararg strings: String?) = throw UnsupportedOperationException()
    override fun getResource(packType: PackType?, resourceLocation: ResourceLocation?) = throw UnsupportedOperationException()
    override fun listResources(
        packType: PackType?,
        string: String?,
        string2: String?,
        resultConsumer: PackResources.ResourceOutput?
    ) = throw UnsupportedOperationException()

    override fun <T : Any?> getMetadataSection(arg: MetadataSectionSerializer<T>) = throw UnsupportedOperationException()
    override fun getNamespaces(resourceType: PackType?) = throw UnsupportedOperationException()

    //#if MC >= 1.20.5
    override fun location(): PackLocationInfo = throw UnsupportedOperationException()
    //#endif
}

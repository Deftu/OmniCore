package dev.deftu.omnicore.internal.resources

import dev.deftu.omnicore.api.resources.ResourcePack
import dev.deftu.omnicore.api.resources.ResourcePackInfo
import dev.deftu.textile.minecraft.MCText
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.metadata.pack.PackMetadataSection
import java.io.InputStream

public class ResourcePackImpl(private val resources: PackResources) : ResourcePack {
    override val info: ResourcePackInfo by lazy {
        //#if MC >= 1.21.9
        val metadata = resources.getMetadataSection(PackMetadataSection.FALLBACK_TYPE)
        //#else
        //$$ val metadata = resources.getMetadataSection(PackMetadataSection.TYPE)
        //#endif

        val id = resources.packId()
        //#if MC >= 1.20.6
        val location = resources.location()
        val title = location.title?.let(MCText::wrap) ?: MCText.literal(id)
        //#else
        //$$ val title = MCText.literal(id)
        //#endif
        val description = metadata?.description?.let(MCText::wrap)

        ResourcePackInfo(id, title, description)
    }

    override fun get(
        type: ResourcePack.Type,
        location: ResourceLocation
    ): InputStream? {
        return resources.getResource(type.packType, location)?.get()
    }

    override fun root(vararg pathSegments: String): InputStream? {
        return resources.getRootResource(*pathSegments)?.get()
    }

    override fun namespaces(type: ResourcePack.Type): Set<String> {
        return resources.getNamespaces(type.packType)
    }

    override fun listResources(
        type: ResourcePack.Type,
        namespace: String,
        path: String,
        filter: ResourcePack.ResourceOutput
    ) {
        resources.listResources(type.packType, namespace, path) { location, _ ->
            filter.accept(location)
        }
    }
}

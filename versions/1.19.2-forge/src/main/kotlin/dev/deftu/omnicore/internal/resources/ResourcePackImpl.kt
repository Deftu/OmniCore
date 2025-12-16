package dev.deftu.omnicore.internal.resources

import dev.deftu.omnicore.api.resources.ResourcePack
import dev.deftu.omnicore.api.resources.ResourcePackInfo
import dev.deftu.textile.minecraft.MCText
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.metadata.pack.PackMetadataSection
import java.io.InputStream

//#if MC <= 1.18.2
//$$ import dev.deftu.omnicore.api.locationOrThrow
//#endif

public class ResourcePackImpl(private val resources: PackResources) : ResourcePack {
    override val info: ResourcePackInfo by lazy {
        val metadata = resources.getMetadataSection(PackMetadataSection.SERIALIZER)

        val id = resources.name
        val title = MCText.literal(id)
        val description = metadata?.description?.let(MCText::wrap)

        ResourcePackInfo(id, title, description)
    }

    override fun get(
        type: ResourcePack.Type,
        location: ResourceLocation
    ): InputStream? {
        return resources.getResource(type.packType, location)
    }

    override fun root(vararg pathSegments: String): InputStream? {
        return resources.getRootResource(pathSegments.joinToString("/"))
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
        resources.getResources(
            type.packType,
            namespace,
            path,
            //#if MC <= 1.18.2
            //$$ Int.MAX_VALUE,
            //#endif
        ) { location ->
            //#if MC <= 1.18.2
            //$$ val location = locationOrThrow(namespace, location)
            //#endif
            filter.accept(location)
            true
        }
    }
}

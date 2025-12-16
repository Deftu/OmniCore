package dev.deftu.omnicore.internal.resources

import dev.deftu.omnicore.api.resources.ResourcePack
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackResources
import java.io.InputStream

public class ResourcePackImpl(private val resources: PackResources) : ResourcePack {
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

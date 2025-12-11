package dev.deftu.omnicore.api.resources

import net.minecraft.resources.ResourceLocation

//#if MC >= 1.16.5
import net.minecraft.server.packs.resources.PreparableReloadListener
//#else
//$$ import net.minecraft.client.resources.IResourceManagerReloadListener
//#endif

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
//#else
//$$ import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener
//#endif
//#endif

public interface ResourceReloadListener
//#if MC >= 1.16.5
    : PreparableReloadListener
//#endif
//#if MC < 1.16.5
//$$ : IResourceManagerReloadListener
//#endif
//#if FABRIC
    , IdentifiableResourceReloadListener
//#endif
{
    public val location: ResourceLocation

    public val dependencies: List<ResourceLocation>
        get() = emptyList()

    //#if FABRIC
    //#if MC >= 1.21.11
    //$$ override fun getFabricId(): Identifier {
    //$$     return this.location
    //$$ }
    //$$
    //$$ override fun getFabricDependencies(): Collection<Identifier> {
    //$$     return this.dependencies
    //$$ }
    //#elseif MC >= 1.16.5
    override fun getFabricId(): ResourceLocation {
        return this.location
    }

    override fun getFabricDependencies(): Collection<ResourceLocation> {
        return this.dependencies
    }
    //#else
    //$$ override fun getFabricId(): net.legacyfabric.fabric.api.util.Identifier {
    //$$     return net.legacyfabric.fabric.api.util.Identifier(this.location.resourceDomain, this.location.resourcePath)
    //$$ }
    //$$
    //$$ override fun getFabricDependencies(): Collection<net.legacyfabric.fabric.api.util.Identifier> {
    //$$     return this.dependencies.map { net.legacyfabric.fabric.api.util.Identifier(it.resourceDomain, it.resourcePath) }
    //$$ }
    //#endif
    //#endif
}

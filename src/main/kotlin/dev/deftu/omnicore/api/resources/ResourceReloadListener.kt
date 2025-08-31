package dev.deftu.omnicore.api.resources

import net.minecraft.util.Identifier

//#if MC >= 1.16.5
import net.minecraft.resource.ResourceReloader
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
    : ResourceReloader
    //#endif
    //#if MC < 1.16.5
    //$$ : IResourceManagerReloadListener
    //#endif
    //#if FABRIC
    , IdentifiableResourceReloadListener
//#endif
{
    public val reloadIdentifier: Identifier

    public val dependencies: List<Identifier>
        get() = emptyList()

    //#if FABRIC
    //#if MC >= 1.16.5
    override fun getFabricId(): Identifier {
        return this.reloadIdentifier
    }

    override fun getFabricDependencies(): Collection<Identifier> {
        return this.dependencies
    }
    //#else
    //$$ override fun getFabricId(): net.legacyfabric.fabric.api.util.Identifier {
    //$$     return net.legacyfabric.fabric.api.util.Identifier(this.reloadIdentifier.namespace, this.reloadIdentifier.path)
    //$$ }
    //$$
    //$$ override fun getFabricDependencies(): Collection<net.legacyfabric.fabric.api.util.Identifier> {
    //$$     return this.dependencies.map { net.legacyfabric.fabric.api.util.Identifier(it.namespace, it.path) }
    //$$ }
    //#endif
    //#endif
}

package dev.deftu.omnicore.api.resources

import dev.deftu.omnicore.api.annotations.VersionedAbove
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

public object OmniServerResources {
    @JvmStatic
    @VersionedAbove("1.16.5")
    public fun registerReloadListener(listener: ResourceReloadListener) {
        //#if FABRIC && MC >= 1.16.5
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(listener)
        //#elseif FORGE-LIKE && MC >= 1.16.5
        //#if MC >= 1.21.4
        //$$ forgeEventBus.addListener<AddServerReloadListenersEvent> { event ->
        //$$     event.addListener(listener.reloadIdentifier, listener)
        //$$ }
        //#else
        //$$ forgeEventBus.addListener<AddReloadListenerEvent> { event ->
        //$$     event.addListener(listener)
        //$$ }
        //#endif
        //#endif
    }
}

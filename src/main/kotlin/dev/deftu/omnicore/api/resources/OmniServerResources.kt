package dev.deftu.omnicore.api.resources

import dev.deftu.omnicore.api.annotations.VersionedAbove

//#if MC >= 1.16.5
//#if FABRIC
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
//#elseif FORGE
//$$ import net.minecraftforge.common.MinecraftForge
//$$ import net.minecraftforge.event.AddReloadListenerEvent
//#else
//$$ import net.neoforged.neoforge.common.NeoForge
//#if MC >= 1.21.4
//$$ import net.neoforged.neoforge.event.AddServerReloadListenersEvent
//#else
//$$ import net.neoforged.neoforge.event.AddReloadListenerEvent
//#endif
//#endif

import net.minecraft.resource.ResourceType
//#else
//#if FABRIC
//$$ import net.legacyfabric.fabric.api.resource.ResourceManagerHelper
//#else
//$$ import net.minecraft.client.resources.IReloadableResourceManager
//#endif
//#endif

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

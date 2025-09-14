package dev.deftu.omnicore.api.client.resources

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.resources.ResourceReloadListener

//#if MC >= 1.16.5
//#if FABRIC
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType
//#elseif FORGE && MC >= 1.17.1
//$$ import net.minecraftforge.client.event.RegisterClientReloadListenersEvent
//#elseif NEOFORGE
//#if MC >= 1.21.4
//$$ import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
//#else
//$$ import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
//#endif
//#endif
//#if FORGE-LIKE
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//#endif
//#else
//#if FABRIC
//$$ import net.legacyfabric.fabric.api.resource.ResourceManagerHelper
//#endif
//#endif

//#if FORGE && MC <= 1.16.5
//$$ import net.minecraft.server.packs.resources.ReloadableResourceManager
//#endif

public object OmniClientResources {
    @JvmStatic
    public fun reload() {
        //#if MC >= 1.16.5
        client.reloadResources()
        //#else
        //$$ client.refreshResources()
        //#endif
    }

    @JvmStatic
    public fun registerReloadListener(listener: ResourceReloadListener) {
        //#if FABRIC
        //#if MC >= 1.16.5
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(listener)
        //#else
        //$$ ResourceManagerHelper.getInstance().registerReloadListener(listener)
        //#endif
        //#elseif MC >= 1.17.1
        //$$ forgeEventBus.addListener<
        //#if MC >= 1.21.4
        //$$         AddClientReloadListenersEvent
        //$$     > { event ->
        //$$         event.addListener(listener.location, listener)
        //$$     }
        //#else
        //$$         RegisterClientReloadListenersEvent
        //$$     > { event ->
        //$$         event.registerReloadListener(listener)
        //$$     }
        //#endif
        //#else
        //$$ val resourceManager = client.resourceManager
        //$$ if (resourceManager is ReloadableResourceManager) {
        //$$     resourceManager.registerReloadListener(listener)
        //$$ }
        //#endif
    }
}

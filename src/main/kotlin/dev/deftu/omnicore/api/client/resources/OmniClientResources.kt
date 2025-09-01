package dev.deftu.omnicore.api.client.resources

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.resources.ResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

public object OmniClientResources {
    @JvmStatic
    public fun reload() {
        client.reloadResources()
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
        //$$         event.addListener(listener.reloadIdentifier, listener)
        //$$     }
        //#else
        //$$         RegisterClientReloadListenersEvent
        //$$     > { event ->
        //$$         event.registerReloadListener(listener)
        //$$     }
        //#endif
        //#else
        //$$ val resourceManager = getInstance().resourceManager
        //$$ if (resourceManager is ReloadableResourceManager) {
        //$$     resourceManager.registerReloadListener(listener)
        //$$ }
        //#endif
    }
}

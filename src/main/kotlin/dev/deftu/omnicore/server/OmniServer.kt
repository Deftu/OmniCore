package dev.deftu.omnicore.server

import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.common.resources.ResourceReloadListener
import net.minecraft.server.MinecraftServer

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

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.client.OmniClient
//$$ import dev.deftu.omnicore.common.OmniLoader
//$$ import net.minecraftforge.fml.server.FMLServerHandler
//#endif

public object OmniServer {

    //#if FABRIC || MC >= 1.16.5
    internal var server: MinecraftServer? = null
    //#endif

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun getInstance(): MinecraftServer? {
        //#if FABRIC || MC >= 1.16.5
        return server
        //#else
        //$$ if (OmniLoader.isPhysicalClient) {
        //$$     return OmniClient.integratedServer
        //$$ }
        //$$
        //$$ return FMLServerHandler.instance().server
        //#endif
    }

    /**
     * Registers a reload listener for the server's resource manager.
     *
     * Only works on Minecraft versions 1.16.5 and above, as 1.12.2 and below don't
     * have a proper resource manager or data reloading system. These systems
     * are only available in the client.
     *
     * @since 0.41.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    @VersionedAbove("1.16.5")
    public fun registerReloadListener(listener: ResourceReloadListener) {
        //#if FABRIC && MC >= 1.16.5
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(listener)
        //#elseif FORGE-LIKE && MC >= 1.16.5
        //#if FORGE
        //$$ MinecraftForge
        //#else
        //$$ NeoForge
        //#endif
        //$$     .EVENT_BUS
        //$$     .addListener<
        //#if MC >= 1.21.4
        //$$         AddServerReloadListenersEvent
        //$$     > { event ->
        //$$         event.addListener(listener.reloadIdentifier, listener)
        //$$     }
        //#else
        //$$         AddReloadListenerEvent
        //$$     > { event ->
        //$$         event.addListener(listener)
        //$$     }
        //#endif
        //#endif
    }

}

package dev.deftu.omnicore.common.events

import dev.deftu.omnicore.OmniCore

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
//#else
//$$ import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerTickEvents
//#endif
//#elseif FORGE
//$$ import net.minecraftforge.common.MinecraftForge
//#if MC <= 1.12.2
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#endif
//#else
//$$ import net.neoforged.neoforge.common.NeoForge
//#endif

//#if FORGE-LIKE
//$$ import dev.deftu.omnicore.server.OmniServer
//#endif

public object OmniCommonEventPassthrough {

    private var isInitialized = false

    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC
        ServerTickEvents.START_SERVER_TICK.register { server ->
            OmniCore.eventBus.post(TickEvent.Server.Pre(server))
        }

        ServerTickEvents.END_SERVER_TICK.register { server ->
            OmniCore.eventBus.post(TickEvent.Server.Post(server))
        }
        //#elseif MC >= 1.20.6
        //$$ NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.event.tick.ServerTickEvent.Pre> { event ->
        //$$     val server = OmniServer.getInstance() ?: return@addListener
        //$$     OmniCore.eventBus.post(TickEvent.Server.Pre(server))
        //$$ }
        //$$
        //$$ NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.event.tick.ServerTickEvent.Post> { event ->
        //$$     val server = OmniServer.getInstance() ?: return@addListener
        //$$     OmniCore.eventBus.post(TickEvent.Server.Post(server))
        //$$ }
        //#elseif MC >= 1.16.5
        //#if FORGE
        //$$ MinecraftForge
        //#else
        //$$ NeoForge
        //#endif
        //$$     .EVENT_BUS.addListener<net.neoforged.neoforge.event.TickEvent.ServerTickEvent> { event ->
        //$$         val server = OmniServer.getInstance() ?: return@addListener
        //$$         when (event.phase) {
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Server.Pre(server))
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Server.Post(server))
        //$$         }
        //$$     }
        //#else
        //$$ MinecraftForge.EVENT_BUS.register(this)
        //#endif

        isInitialized = true
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onServerTick(event: net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent) {
    //$$     val server = OmniServer.getInstance() ?: return
    //$$     when (event.phase) {
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Server.Pre(server))
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Server.Post(server))
    //$$     }
    //$$ }
    //#endif

}

package dev.deftu.omnicore.internal.events

import dev.deftu.omnicore.api.eventBus
import dev.deftu.omnicore.api.events.ServerTickEvent
import org.jetbrains.annotations.ApiStatus

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
//$$ import dev.deftu.omnicore.api.server
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//#endif

@ApiStatus.Internal
public object ServerTickEventForwarding {
    private var isInitialized = false

    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC
        ServerTickEvents.START_SERVER_TICK.register { server ->
            eventBus.post(ServerTickEvent.Pre(server))
        }

        ServerTickEvents.END_SERVER_TICK.register { server ->
            eventBus.post(ServerTickEvent.Post(server))
        }
        //#elseif MC >= 1.20.6
        //$$ NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.event.tick.ServerTickEvent.Pre> { event ->
        //$$     val server = server ?: return@addListener
        //$$     eventBus.post(ServerTickEvent.Pre(server))
        //$$ }
        //$$
        //$$ NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.event.tick.ServerTickEvent.Post> { event ->
        //$$     val server = server ?: return@addListener
        //$$     eventBus.post(ServerTickEvent.Post(server))
        //$$ }
        //#elseif MC >= 1.16.5
        //$$ forgeEventBus.addListener<net.neoforged.neoforge.event.TickEvent.ServerTickEvent> { event ->
        //$$         val server = server ?: return@addListener
        //$$         when (event.phase) {
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.START -> eventBus.post(ServerTickEvent.Pre(server))
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.END -> eventBus.post(ServerTickEvent.Post(server))
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
    //$$     val server = server ?: return
    //$$     when (event.phase) {
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> eventBus.post(ServerTickEvent.Pre(server))
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> eventBus.post(ServerTickEvent.Post(server))
    //$$     }
    //$$ }
    //#endif
}

package dev.deftu.omnicore.internal.client.events

import dev.deftu.omnicore.api.client.events.ClientTickEvent
import dev.deftu.omnicore.api.eventBus
import org.jetbrains.annotations.ApiStatus

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//#else
//$$ import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//#endif
//#else
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//#if MC < 1.16.5
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#endif
//#endif

@ApiStatus.Internal
public object ClientTickEventForwarding {
    private var isInitialized = false

    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC
        ClientTickEvents.START_CLIENT_TICK.register { _ ->
            eventBus.post(ClientTickEvent.Pre)
        }

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            eventBus.post(ClientTickEvent.Post)
        }
        //#elseif MC >= 1.16.5
        //#if MC >= 1.20.6
        //$$ forgeEventBus.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Pre> { event ->
        //$$     eventBus.post(ClientTickEvent.Pre)
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Post> { event ->
        //$$     eventBus.post(ClientTickEvent.Post)
        //$$ }
        //#elseif MC >= 1.16.5
        //$$ forgeEventBus.addListener<net.neoforged.neoforge.event.TickEvent.ClientTickEvent> { event ->
        //$$         when (event.phase) {
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.START -> eventBus.post(ClientTickEvent.Pre)
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.END -> eventBus.post(ClientTickEvent.Post)
        //$$         }
        //$$     }
        //#endif
        //#else
        //$$ forgeEventBus.register(this)
        //#endif

        isInitialized = true
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onClientTick(event: net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent) {
    //$$     when (event.phase) {
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> eventBus.post(ClientTickEvent.Pre)
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> eventBus.post(ClientTickEvent.Post)
    //$$         else -> {
    //$$             // no-op
    //$$         }
    //$$     }
    //$$ }
    //#endif
}

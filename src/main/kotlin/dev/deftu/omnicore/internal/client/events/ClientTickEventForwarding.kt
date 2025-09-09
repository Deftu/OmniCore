package dev.deftu.omnicore.internal.client.events

import dev.deftu.omnicore.api.client.events.ClientTickEvent
import dev.deftu.omnicore.api.eventBus
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.jetbrains.annotations.ApiStatus

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
        //$$ EventHolder.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Pre> { event ->
        //$$     OmniCore.eventBus.post(TickEvent.Client.Pre)
        //$$ }
        //$$
        //$$ EventHolder.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Post> { event ->
        //$$     OmniCore.eventBus.post(TickEvent.Client.Post)
        //$$ }
        //#elseif MC >= 1.16.5
        //$$ EventHolder.EVENT_BUS.addListener<net.neoforged.neoforge.event.TickEvent.ClientTickEvent> { event ->
        //$$         when (event.phase) {
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Client.Pre)
        //$$             net.neoforged.neoforge.event.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Client.Post)
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
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Client.Pre)
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Client.Post)
    //$$         else -> {
    //$$             // no-op
    //$$         }
    //$$     }
    //$$ }
    //#endif
}

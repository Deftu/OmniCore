package dev.deftu.omnicore.common

import dev.deftu.eventbus.on
import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.events.RenderTickEvent
import dev.deftu.omnicore.common.events.TickEvent

public object TickSchedulers {

    @JvmStatic
    @GameSide(Side.BOTH)
    public val server: TickScheduler = DefaultTickScheduler("server")
        @JvmName("server")
        get

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val client: TickScheduler = DefaultTickScheduler("client")
        @JvmName("client")
        get

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val render: TickScheduler = DefaultTickScheduler("render")
        @JvmName("render")
        get

    public fun initialize() {
        OmniCore.eventBus.on<TickEvent.Server.Post> {
            (server as DefaultTickScheduler).tick()
        }

        OmniCore.eventBus.on<TickEvent.Client.Post> {
            (client as DefaultTickScheduler).tick()
        }

        OmniCore.eventBus.on<RenderTickEvent.Post> {
            (render as DefaultTickScheduler).tick()
        }
    }

}

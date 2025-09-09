package dev.deftu.omnicore.api.scheduling

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.client.events.ClientTickEvent
import dev.deftu.omnicore.api.client.events.RenderTickEvent
import dev.deftu.omnicore.api.eventBus
import dev.deftu.omnicore.api.events.ServerTickEvent
import dev.deftu.omnicore.internal.scheduling.DefaultTickScheduler

public object TickSchedulers {
    @JvmStatic
    public val client: TickScheduler = DefaultTickScheduler("client")
        @JvmName("client")
        get

    @JvmStatic
    public val server: TickScheduler = DefaultTickScheduler("server")
        @JvmName("server")
        get

    @JvmStatic
    public val render: TickScheduler = DefaultTickScheduler("render")
        @JvmName("render")
        get

    public fun initialize() {
        eventBus.on<ServerTickEvent.Post> {
            (this@TickSchedulers.server as DefaultTickScheduler).tick()
        }

        eventBus.on<ClientTickEvent.Post> {
            (this@TickSchedulers.client as DefaultTickScheduler).tick()
        }

        eventBus.on<RenderTickEvent.Post> {
            (this@TickSchedulers.render as DefaultTickScheduler).tick()
        }
    }
}

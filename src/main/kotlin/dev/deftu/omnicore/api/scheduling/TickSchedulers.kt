package dev.deftu.omnicore.api.scheduling

import dev.deftu.eventbus.on
import dev.deftu.omnicore.internal.scheduling.DefaultTickScheduler
import dev.deftu.omnicore.common.events.TickEvent

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
        OmniCore.eventBus.on<TickEvent.Server.Post> {
            (this@TickSchedulers.server as DefaultTickScheduler).tick()
        }

        OmniCore.eventBus.on<TickEvent.Client.Post> {
            (this@TickSchedulers.client as DefaultTickScheduler).tick()
        }

        OmniCore.eventBus.on<RenderTickEvent.Post> {
            (this@TickSchedulers.render as DefaultTickScheduler).tick()
        }
    }
}

package dev.deftu.omnicore.internal.time

import dev.deftu.omnicore.api.time.TickSource
import net.minecraft.server.MinecraftServer

public class ServerTickSource(private val server: MinecraftServer) : TickSource {
    override fun ticks(): Long {
        return server.tickCount.toLong()
    }
}

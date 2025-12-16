package dev.deftu.omnicore.internal.time

import dev.deftu.omnicore.api.time.TickSource
import net.minecraft.world.level.Level

public class LevelTickSource(private val level: Level) : TickSource {
    override fun ticks(): Long {
        return level.gameTime
    }
}
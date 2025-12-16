package dev.deftu.omnicore.api.time

import dev.deftu.omnicore.internal.time.LevelTickSource
import dev.deftu.omnicore.internal.time.ServerTickSource
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.Level

public object TickSources {
    @JvmField public val ZERO: TickSource = TickSource { 0L }

    @JvmStatic
    public fun min(first: TickSource, second: TickSource): TickSource {
        return TickSource { minOf(first.ticks(), second.ticks()) }
    }

    @JvmStatic
    public fun max(first: TickSource, second: TickSource): TickSource {
        return TickSource { maxOf(first.ticks(), second.ticks()) }
    }

    @JvmStatic
    public fun offset(source: TickSource, offset: Long): TickSource {
        return TickSource { source.ticks() + offset }
    }

    @JvmStatic
    public fun scaled(source: TickSource, factor: Double): TickSource {
        return TickSource { (source.ticks() * factor).toLong() }
    }

    @JvmStatic
    public fun fixed(ticks: Long): TickSource {
        return TickSource { ticks }
    }

    @JvmStatic
    @JvmOverloads
    public fun counter(initial: Long = 0L): MutableTickSource {
        return MutableTickSource(initial)
    }

    @JvmStatic
    @JvmOverloads
    public fun monotonic(clock: MonotonicClock = MonotonicClock.SYSTEM): TickSource {
        val start = clock.nowNanos()
        return TickSource {
            val elapsedMs = clock.elapsedMillis(start)
            TickTime.millisToTicks(elapsedMs)
        }
    }

    @JvmStatic
    public fun server(server: MinecraftServer): TickSource {
        return ServerTickSource(server)
    }

    @JvmStatic
    public fun level(level: Level): TickSource {
        return LevelTickSource(level)
    }
}

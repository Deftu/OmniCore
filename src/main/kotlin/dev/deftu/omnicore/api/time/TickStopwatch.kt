package dev.deftu.omnicore.api.time

public class TickStopwatch(start: Long = 0L) {
    public var start: Long = start.coerceAtLeast(0)
        private set

    public fun reset(current: Long = 0L) {
        start = current.coerceAtLeast(0)
    }

    public fun reset(source: TickSource) {
        reset(source.ticks())
    }

    public fun elapsed(current: Long): Long {
        val now = current.coerceAtLeast(0)
        return (now - start).coerceAtLeast(0)
    }

    public fun elapsed(source: TickSource): Long {
        return elapsed(source.ticks())
    }

    public fun elapsedMillis(current: Long): Long {
        return TickTime.ticksToMillis(elapsed(current))
    }
}

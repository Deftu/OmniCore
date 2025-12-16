package dev.deftu.omnicore.api.time

public class TickRateLimiter(interval: Long) {
    public val interval: Long = interval.coerceAtLeast(1L)

    public var lastAcquired: Long = Long.MIN_VALUE
        private set

    @JvmOverloads
    public fun primed(start: Long = 0L): TickRateLimiter {
        lastAcquired = start.coerceAtLeast(0L)
        return this
    }

    public fun primed(source: TickSource): TickRateLimiter {
        return primed(source.ticks())
    }

    public fun tryAcquire(currentTick: Long): Boolean {
        val now = currentTick.coerceAtLeast(0L)
        val last = lastAcquired
        if (last == Long.MIN_VALUE) {
            lastAcquired = now
            return true
        }

        if (now < last) {
            lastAcquired = now
            return true
        }

        val elapsed = now - last
        if (elapsed >= interval) {
            lastAcquired = now
            return true
        }

        return false
    }

    public fun tryAcquire(source: TickSource): Boolean {
        return tryAcquire(source.ticks())
    }

    public fun reset() {
        lastAcquired = Long.MIN_VALUE
    }
}

package dev.deftu.omnicore.api.time

import dev.deftu.omnicore.internal.time.SystemMonotonicClock

public interface MonotonicClock {
    public companion object {
        @JvmField public val SYSTEM: MonotonicClock = SystemMonotonicClock
    }

    public fun nowNanos(): Long
    public fun elapsedNanos(since: Long): Long
    public fun elapsedMillis(since: Long): Long
    public fun deadlineAfterMillis(delayMs: Long): Long
    public fun isExpired(deadlineNanos: Long): Boolean
}

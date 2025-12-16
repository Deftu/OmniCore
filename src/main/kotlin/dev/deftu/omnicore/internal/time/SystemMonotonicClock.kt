package dev.deftu.omnicore.internal.time

import dev.deftu.omnicore.api.time.MonotonicClock
import kotlin.math.max

public object SystemMonotonicClock : MonotonicClock {
    override fun nowNanos(): Long {
        return System.nanoTime()
    }

    override fun elapsedNanos(since: Long): Long {
        return max(0L, nowNanos() - since)
    }

    override fun elapsedMillis(since: Long): Long {
        return elapsedNanos(since) / 1_000_000L
    }

    override fun deadlineAfterMillis(delayMs: Long): Long {
        val delayNanos = max(0L, delayMs) * 1_000_000L
        return nowNanos() + delayNanos
    }

    override fun isExpired(deadlineNanos: Long): Boolean {
        return nowNanos() >= deadlineNanos
    }
}

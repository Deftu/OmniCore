package dev.deftu.omnicore.api.time

public class MutableTickSource(initial: Long = 0L) : TickSource {
    private var value: Long = initial.coerceAtLeast(0L)

    public fun set(ticks: Long) {
        value = ticks.coerceAtLeast(0L)
    }

    public fun increment(by: Long = 1L) {
        value = (value + by).coerceAtLeast(0L)
    }

    override fun ticks(): Long {
        return value
    }
}

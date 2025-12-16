package dev.deftu.omnicore.api.time

public class TickDebouncer(delay: Long) {
    public val delay: Long = delay.coerceAtLeast(0)

    public var lastTrigger: Long = Long.MIN_VALUE
        private set
    public var firedFor: Long = Long.MIN_VALUE
        private set

    public fun trigger(current: Long) {
        lastTrigger = current.coerceAtLeast(0)
    }

    public fun trigger(source: TickSource) {
        trigger(source.ticks())
    }

    public fun shouldRun(current: Long): Boolean {
        val now = current.coerceAtLeast(0)
        val last = lastTrigger
        if (last == Long.MIN_VALUE) {
            return false
        }

        if (now < last) {
            lastTrigger = now
            firedFor = Long.MIN_VALUE
            return false
        }

        val readyTick = last + delay
        if (now < readyTick) {
            return false
        }

        if (firedFor == last) {
            return false
        }

        firedFor = last
        return true
    }

    public fun shouldRun(source: TickSource): Boolean {
        return shouldRun(source.ticks())
    }

    public fun reset() {
        lastTrigger = Long.MIN_VALUE
        firedFor = Long.MIN_VALUE
    }
}

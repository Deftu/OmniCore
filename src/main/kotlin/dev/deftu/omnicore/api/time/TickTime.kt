package dev.deftu.omnicore.api.time

public object TickTime {
    public const val TICKS_PER_SECOND: Int = 20
    public const val MILLIS_PER_TICK: Long = 50L

    public fun ticksToMillis(ticks: Long): Long {
        return ticks * MILLIS_PER_TICK
    }

    public fun millisToTicks(millis: Long): Long {
        return millis / MILLIS_PER_TICK
    }

    public fun secondsToTicks(seconds: Long): Long {
        return seconds * TICKS_PER_SECOND
    }

    public fun ticksToSeconds(ticks: Long): Long {
        return ticks / TICKS_PER_SECOND
    }

    public fun clamp(ticks: Long): Long {
        return if (ticks < 0L) 0L else ticks
    }

    public fun format(ticks: Long): String {
        val totalSeconds = ticksToSeconds(ticks)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}

package dev.deftu.omnicore.api.client.render

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.client.events.RenderTickEvent
import dev.deftu.omnicore.api.eventBus

public object OmniFrameClock {
    private var lastFrameTimeNanos: Long = System.nanoTime()
    private var lastFrameDurationNanos: Long = 0L

    private val frameDurations = ArrayDeque<Long>(100)

    /**
     * Returns the most recent frame duration in milliseconds.
     * May be inaccurate if not running in the render loop.
     */
    @JvmStatic
    public val lastFrameTimeMillis: Double
        get() = lastFrameDurationNanos / 1_000_000.0

    /**
     * Returns the average frame duration over the last 100 frames in milliseconds.
     */
    @JvmStatic
    public val averageFrameTimeMillis: Double
        get() = if (frameDurations.isEmpty()) 0.0 else frameDurations.average() / 1_000_000.0

    @JvmStatic
    public fun initialize() {
        // Reset the frame time tracking
        lastFrameTimeNanos = System.nanoTime()
        lastFrameDurationNanos = 0L
        frameDurations.clear()

        eventBus.on<RenderTickEvent.Pre> {
            // Calculate the time since the last frame
            val currentTimeNanos = System.nanoTime()
            lastFrameDurationNanos = currentTimeNanos - lastFrameTimeNanos
            lastFrameTimeNanos = currentTimeNanos

            // Add the frame duration to the deque
            frameDurations.addLast(lastFrameDurationNanos)
            if (frameDurations.size > 100) {
                frameDurations.removeFirst()
            }
        }
    }
}

package dev.deftu.omnicore.api.client.render

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.client.events.RenderTickEvent
import dev.deftu.omnicore.api.eventBus

public object OmniFrameClock {
    public const val MAX_FRAME_SAMPLES: Int = 100
    private const val EMA_ALPHA = 2.0 / (MAX_FRAME_SAMPLES + 1.0)

    private var isInitialized = false

    private var lastFrameTimestampNanos = 0L
    private var lastFrameDurationNanos = 0L

    private val frameDurations = ArrayDeque<Long>(MAX_FRAME_SAMPLES)
    private var emaFrameDurationNanos = 0.0
    private var minFrameDurationNanos = Long.MAX_VALUE
    private var maxFrameDurationNanos = 0L

    /** Most recent frame duration in milliseconds. */
    @JvmStatic
    public val lastFrameTimeMillis: Double
        get() = lastFrameDurationNanos / 1_000_000.0

    /** Average frame duration over the last [MAX_FRAME_SAMPLES] frames (ms) */
    @JvmStatic
    public val averageFrameTimeMillis: Double
        get() = if (frameDurations.isEmpty()) 0.0 else frameDurations.average() / 1_000_000.0

    /** Exponentially moving average frame duration (ms) */
    @JvmStatic
    public val emaFrameTimeMillis: Double
        get() = emaFrameDurationNanos / 1_000_000.0

    /** FPS based on the last frame duration. */
    @JvmStatic
    public val lastFps: Double
        get() = if (lastFrameDurationNanos == 0L) 0.0 else 1e9 / lastFrameDurationNanos

    /** Average FPS over the last [MAX_FRAME_SAMPLES] frames. */
    @JvmStatic
    public val averageFps: Double
        get() = if (frameDurations.isEmpty()) 0.0 else 1e9 / (frameDurations.average())

    /** Exponentially moving average FPS. */
    @JvmStatic
    public val emaFps: Double
        get() = if (emaFrameDurationNanos <= 0.0) 0.0 else 1e9 / emaFrameDurationNanos

    /** Number of frame samples recorded, up to [MAX_FRAME_SAMPLES]. */
    @JvmStatic
    public val sampleCount: Int
        get() = frameDurations.size

    /** Minimum frame duration recorded (ms). */
    @JvmStatic
    public val minFrameTimeMillis: Double
        get() = if (minFrameDurationNanos == Long.MAX_VALUE) 0.0 else minFrameDurationNanos / 1_000_000.0

    /** Maximum frame duration recorded (ms). */
    @JvmStatic
    public val maxFrameTimeMillis: Double
        get() = maxFrameDurationNanos / 1_000_000.0

    @JvmStatic
    public fun initialize() {
        if (isInitialized) {
            return
        }

        // Reset the frame time tracking
        lastFrameTimestampNanos = System.nanoTime()
        lastFrameDurationNanos = 0L
        frameDurations.clear()
        emaFrameDurationNanos = 0.0
        minFrameDurationNanos = Long.MAX_VALUE
        maxFrameDurationNanos = 0L

        eventBus.on<RenderTickEvent.Pre> {
            val now = System.nanoTime()
            val duration = now - lastFrameTimestampNanos

            // Calculate the time since the last frame
            lastFrameTimestampNanos = now
            lastFrameDurationNanos = duration
            recordFrameDuration(duration)
        }

        isInitialized = true
    }

    private fun recordFrameDuration(duration: Long) {
        if (frameDurations.size == MAX_FRAME_SAMPLES) {
            val evictedDuration = frameDurations.removeFirst()
            if (evictedDuration == minFrameDurationNanos || evictedDuration == maxFrameDurationNanos) {
                recomputeExtrema()
            }
        }

        frameDurations.addLast(duration)

        if (duration < minFrameDurationNanos) {
            minFrameDurationNanos = duration
        }

        if (duration > maxFrameDurationNanos) {
            maxFrameDurationNanos = duration
        }

        emaFrameDurationNanos = if (emaFrameDurationNanos == 0.0) {
            duration.toDouble()
        } else {
            val alpha = EMA_ALPHA
            (duration * alpha) + (emaFrameDurationNanos * (1.0 - alpha))
        }
    }

    private fun recomputeExtrema() {
        var min = Long.MAX_VALUE
        var max = 0L
        for (duration in frameDurations) {
            if (duration < min) {
                min = duration
            }

            if (duration > max) {
                max = duration
            }
        }

        minFrameDurationNanos = min
        maxFrameDurationNanos = max
    }
}

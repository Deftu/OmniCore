package dev.deftu.omnicore.common

import kotlinx.atomicfu.atomic
import org.apache.logging.log4j.LogManager
import java.util.PriorityQueue
import java.util.concurrent.ConcurrentLinkedQueue

public class DefaultTickScheduler(private val name: String, ) : TickScheduler {
    private companion object {
        private val logger = LogManager.getLogger(TickScheduler::class.java)
    }

    private data class ScheduledHandle(
        var whenTick: Long,
        val period: Int, // 0 = one-shot, >0 = repeating
        val isFixedRate: Boolean, // fixed-rate vs fixed-delay
        val runnable: Runnable,
    ) : TickScheduler.Handle {
        override var isCancelled by atomic(false)
            private set
        override fun cancel() { isCancelled = true }
    }

    private val immediate = ConcurrentLinkedQueue<Runnable>()
    private val scheduled = PriorityQueue<ScheduledHandle>(compareBy(ScheduledHandle::whenTick))

    // Drainage guards
    private var isTicking by atomic(false)
    private var tickCounter: Long = 0

    override fun post(runnable: Runnable) {
        immediate.add(runnable)
    }

    override fun after(delay: Int, runnable: Runnable): TickScheduler.Handle {
        require(delay >= 0) { "Delay must be non-negative" }
        // You can't run immediately... Lol
        val target = tickCounter + maxOf(1, delay)
        val handle = ScheduledHandle(target, 0, false, runnable)
        synchronized(scheduled) { scheduled.add(handle) }
        return handle
    }

    override fun every(
        period: Int,
        runnable: Runnable,
        initialDelay: Int,
        isFixedRate: Boolean
    ): TickScheduler.Handle {
        require(period > 0) { "Period must be positive" }
        require(initialDelay >= 0) { "Initial delay must be non-negative" }
        val first = tickCounter + maxOf(1, initialDelay.takeIf { it > 0 } ?: period)
        val handle = ScheduledHandle(first, period, isFixedRate, runnable)
        synchronized(scheduled) { scheduled.add(handle) }
        return handle
    }

    /**
     * Advance one tick on this timeline and execute due tasks.
     * Call this from your END phase tick/render hook for the timeline.
     */
    public fun tick() {
        if (isTicking) {
            logger.warn("TickScheduler $name is already ticking; skipping to avoid reentrancy")
            return
        }

        isTicking = true

        try {
            tickCounter++

            // Run immediate tasks first, high priority
            while (true) {
                val task = immediate.poll() ?: break
                try {
                    task.run()
                } catch (e: Throwable) {
                    logger.error("Error executing immediate task in $name", e)
                }
            }

            // Process scheduled tasks in order of their absolute tick time requested
            while (true) {
                val next = synchronized(scheduled) { scheduled.peek() } ?: break
                if (next.whenTick > tickCounter) {
                    break
                }

                synchronized(scheduled) { scheduled.poll() } // remove it
                if (next.isCancelled) {
                    continue
                }

                try {
                    next.runnable.run()
                } catch (e: Throwable) {
                    logger.error("Error executing scheduled task in $name", e)
                }

                // Reschedule repeating task if not cancelled
                if (!next.isCancelled && next.period > 0) {
                    next.whenTick = if (next.isFixedRate) {
                        // Schedule based on previous target time (no drift)
                        next.whenTick + next.period
                    } else {
                        // Schedule based on completion time (fixed delay)
                        tickCounter + next.period
                    }

                    synchronized(scheduled) { scheduled.add(next) }
                }
            }
        } finally {
            isTicking = false
        }
    }
}

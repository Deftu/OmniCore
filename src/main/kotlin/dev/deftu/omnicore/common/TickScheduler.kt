package dev.deftu.omnicore.common

public interface TickScheduler {

    public interface Handle {
        public val isCancelled: Boolean

        /**
         * @author Deftu
         * @since 0.41.0
         */
        public fun cancel()
    }

    /**
     * Run on the next tick of this scheduler’s timeline.
     *
     * @author Deftu
     * @since 0.41.0
     */
    public fun post(runnable: Runnable)

    /**
     * Run after [delay] ticks.
     *
     * @return a handle you can cancel.
     * @author Deftu
     * @since 0.41.0
     */
    public fun after(delay: Int, runnable: Runnable): Handle

    /**
     * Run repeatedly.
     *
     * @param period ticks between each execution.
     * @param runnable the task to run.
     *
     * @return A handle you can use to cancel the scheduled task.
     */
    public fun every(
        period: Int,
        runnable: Runnable,
        initialDelay: Int = period,
        isFixedRate: Boolean = true
    ): Handle

}

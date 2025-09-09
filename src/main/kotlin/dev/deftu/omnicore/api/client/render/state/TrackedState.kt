package dev.deftu.omnicore.api.client.render.state

public sealed interface TrackedState<T : TrackedState<T>> {
    public val prevState: T?

    public fun submit(saveLast: Boolean = true)

    /** Restores the previous state captured when [submit] was called. No-op if [submit] was never called. */
    public fun restore() {
        prevState?.submit(false)
    }
}

package dev.deftu.omnicore.api.client.render.state

public interface TrackedState<T : TrackedState<T>> {
    public val prevState: T?

    public fun submit(saveLast: Boolean)

    public fun submit() {
        submit(true)
    }

    /** Restores the previous state captured when [submit] was called. No-op if [submit] was never called. */
    public fun restore() {
        prevState?.submit(false)
    }
}

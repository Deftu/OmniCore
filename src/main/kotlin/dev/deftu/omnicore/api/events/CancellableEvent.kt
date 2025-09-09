package dev.deftu.omnicore.api.events

public interface CancellableEvent {
    public var isCancelled: Boolean

    public fun cancel() {
        isCancelled = true
    }
}

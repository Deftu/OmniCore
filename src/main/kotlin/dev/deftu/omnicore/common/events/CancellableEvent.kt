package dev.deftu.omnicore.common.events

public interface CancellableEvent {

    public var isCancelled: Boolean

    public fun cancel() {
        isCancelled = true
    }

}

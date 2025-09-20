package dev.deftu.omnicore.internal.events

import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object EventForwarding {
    public fun initialize() {
        ServerTickEventForwarding.initialize()
    }
}

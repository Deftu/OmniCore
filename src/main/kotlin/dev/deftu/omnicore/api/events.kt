@file:JvmName("OmniCoreEvents")

package dev.deftu.omnicore.api

import dev.deftu.eventbus.EventBus
import dev.deftu.eventbus.bus
import dev.deftu.eventbus.invokers.LMFInvoker

@get:JvmName("get")
public val eventBus: EventBus = bus {
    invoker = LMFInvoker()
    threadSafety = true // Slightly decreases performance, but ensures stability
}

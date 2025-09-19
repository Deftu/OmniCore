@file:JvmName("OmniCore")

package dev.deftu.omnicore.api

import dev.deftu.eventbus.EventBus
import dev.deftu.eventbus.bus
import dev.deftu.eventbus.invokers.LMFInvoker

public const val ID: String = "@MOD_ID@"
public const val VERSION: String = "@MOD_VERSION@"
public const val GIT_BRANCH: String = "@GIT_BRANCH@"
public const val GIT_COMMIT: String = "@GIT_COMMIT@"
public const val GIT_URL: String = "@GIT_URL@"

public val eventBus: EventBus = bus {
    invoker = LMFInvoker()
    threadSafety = true // Slightly decreases performance, but ensures stability
}

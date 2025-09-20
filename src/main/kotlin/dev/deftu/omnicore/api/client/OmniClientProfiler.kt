@file:JvmName("OmniClientProfiler")

package dev.deftu.omnicore.api.client

import net.minecraft.client.MinecraftClient
import net.minecraft.util.profiler.Profiler

//#if MC >= 1.21.2
import net.minecraft.util.profiler.Profilers
//#endif

public inline val MinecraftClient.builtinProfiler: Profiler
    @JvmName("get")
    get() {
        //#if MC >= 1.21.2
        return Profilers.get()
        //#else
        //$$ return client.profiler
        //#endif
    }

@JvmName("start")
public fun MinecraftClient.startProfiler(name: String) {
    builtinProfiler.push(name)
}

@JvmName("end")
public fun MinecraftClient.endProfiler() {
    builtinProfiler.pop()
}

@JvmName("swap")
public fun MinecraftClient.swapProfiler(name: String) {
    builtinProfiler.swap(name)
}

@JvmName("withProfiler")
public inline fun MinecraftClient.profiled(name: String, crossinline block: () -> Unit) {
    try {
        try {
            swapProfiler(name)
        } catch (_: Exception) {
            startProfiler(name)
        }

        block()
    } finally {
        endProfiler()
    }
}

@JvmName("withProfiler")
public fun MinecraftClient.profiled(name: String, block: Runnable) {
    try {
        try {
            swapProfiler(name)
        } catch (_: Exception) {
            startProfiler(name)
        }

        block.run()
    } finally {
        endProfiler()
    }
}

@JvmName("withProfiler")
public inline fun <T> MinecraftClient.profiled(name: String, crossinline block: () -> T): T {
    return try {
        try {
            swapProfiler(name)
        } catch (_: Exception) {
            startProfiler(name)
        }

        block()
    } finally {
        endProfiler()
    }
}

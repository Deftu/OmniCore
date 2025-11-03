@file:JvmName("OmniClientProfiler")

package dev.deftu.omnicore.api.client

import net.minecraft.client.Minecraft
import net.minecraft.util.profiling.ProfilerFiller

//#if MC >= 1.21.2
import net.minecraft.util.profiling.Profiler
//#endif

public inline val Minecraft.builtinProfiler: ProfilerFiller
    @JvmName("get")
    get() {
        //#if MC >= 1.21.2
        return Profiler.get()
        //#else
        //$$ return client.profiler
        //#endif
    }

@JvmName("start")
public fun Minecraft.startProfiler(name: String) {
    builtinProfiler.push(name)
}

@JvmName("end")
public fun Minecraft.endProfiler() {
    builtinProfiler.pop()
}

@JvmName("swap")
public fun Minecraft.swapProfiler(name: String) {
    builtinProfiler.popPush(name)
}

@JvmName("withProfiler")
public inline fun Minecraft.profiled(name: String, crossinline block: () -> Unit) {
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
public fun Minecraft.profiled(name: String, block: Runnable) {
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
public inline fun <T> Minecraft.profiled(name: String, crossinline block: () -> T): T {
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

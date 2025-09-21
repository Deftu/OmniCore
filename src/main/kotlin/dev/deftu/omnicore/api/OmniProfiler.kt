@file:JvmName("OmniProfiler")

package dev.deftu.omnicore.api

import net.minecraft.server.MinecraftServer
import net.minecraft.util.profiler.Profiler

//#if MC >= 1.21.2
import net.minecraft.util.profiler.Profilers
//#endif

public inline val MinecraftServer.builtinProfiler: Profiler
    @JvmName("get")
    get() {
        //#if MC >= 1.21.2
        return Profilers.get()
        //#else
        //$$ return profiler
        //#endif
    }

@JvmName("start")
public fun MinecraftServer.startProfiler(name: String) {
    builtinProfiler.push(name)
}

@JvmName("end")
public fun MinecraftServer.endProfiler() {
    builtinProfiler.pop()
}

@JvmName("swap")
public fun MinecraftServer.swapProfiler(name: String) {
    builtinProfiler.swap(name)
}

@JvmName("withProfiler")
public inline fun MinecraftServer.profiled(name: String, crossinline block: () -> Unit) {
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
public fun MinecraftServer.profiled(name: String, block: Runnable) {
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
public inline fun <T> MinecraftServer.profiled(name: String, crossinline block: () -> T): T {
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

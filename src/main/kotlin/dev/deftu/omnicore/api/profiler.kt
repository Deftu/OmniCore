@file:JvmName("OmniProfiler")

package dev.deftu.omnicore.api

import net.minecraft.util.profiler.Profiler

//#if MC >= 1.21.2
import net.minecraft.util.profiler.Profilers
//#endif

public inline val profiler: Profiler
    @JvmName("get")
    get() {
        //#if MC >= 1.21.2
        return Profilers.get()
        //#else
        //$$ return OmniClient.getInstance().profiler
        //#endif
    }

@JvmName("start")
public fun startProfiler(name: String) {
    profiler.push(name)
}

@JvmName("end")
public fun endProfiler() {
    profiler.pop()
}

@JvmName("swap")
public fun swapProfiler(name: String) {
    profiler.swap(name)
}

@JvmName("withProfiler")
public inline fun profiled(name: String, crossinline block: () -> Unit) {
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
public fun profiled(name: String, block: Runnable) {
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
public inline fun <T> profiled(name: String, crossinline block: () -> T): T {
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

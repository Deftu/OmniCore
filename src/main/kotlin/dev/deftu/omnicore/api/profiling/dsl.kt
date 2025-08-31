package dev.deftu.omnicore.api.profiling

public inline fun profiled(name: String, crossinline block: () -> Unit) {
    OmniProfiler.withProfiler(name) {
        block()
    }
}

public inline fun <T> profiled(name: String, crossinline block: () -> T): T {
    return OmniProfiler.withProfiler(name) {
        block()
    }
}

package dev.deftu.omnicore.internal

internal inline val Throwable.asReadableString: String
    get() {
        return this.stackTraceToString()
            .replace("\r\n", "\n")
            .replace("\t", " ".repeat(4))
    }

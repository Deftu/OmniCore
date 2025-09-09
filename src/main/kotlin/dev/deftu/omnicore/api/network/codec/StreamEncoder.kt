package dev.deftu.omnicore.api.network.codec

public fun interface StreamEncoder<O, T> {
    public fun encode(output: O, value: T)
}

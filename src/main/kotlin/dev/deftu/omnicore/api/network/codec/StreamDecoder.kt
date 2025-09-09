package dev.deftu.omnicore.api.network.codec

public fun interface StreamDecoder<I, T> {
    public fun decode(input: I): T
}

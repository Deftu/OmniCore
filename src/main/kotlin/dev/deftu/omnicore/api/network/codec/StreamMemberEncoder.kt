package dev.deftu.omnicore.api.network.codec

public fun interface StreamMemberEncoder<O, T> {
    public fun encode(value: T, output: O)
}

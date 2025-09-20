package dev.deftu.omnicore.api.nbt

public class NoSuchNbtElementException(key: String, type: NbtType) :
    NoSuchElementException("No such NBT element with key '$key' and type '${type.name}'")

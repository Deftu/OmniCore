package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.StringReader

public interface ArgumentTypeVariant<T> {
    public fun parse(reader: StringReader): T?
}

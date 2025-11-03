package dev.deftu.omnicore.api.serialization

import com.mojang.serialization.DynamicOps
import net.minecraft.nbt.Tag
import net.minecraft.nbt.NbtOps

public object NbtOps {
    @JvmStatic
    public fun get(): DynamicOps<Tag> {
        return NbtOps.INSTANCE
    }
}

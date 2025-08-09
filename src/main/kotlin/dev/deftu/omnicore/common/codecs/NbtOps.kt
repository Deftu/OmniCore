package dev.deftu.omnicore.common.codecs

import com.mojang.serialization.DynamicOps
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps

public object NbtOps {

    @JvmStatic
    public fun get(): DynamicOps<NbtElement> {
        return NbtOps.INSTANCE
    }

}

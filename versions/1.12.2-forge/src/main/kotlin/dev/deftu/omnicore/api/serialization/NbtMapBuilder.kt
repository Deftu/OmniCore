package dev.deftu.omnicore.api.serialization

import com.mojang.serialization.DataResult
import com.mojang.serialization.RecordBuilder
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagEnd

internal object NbtMapBuilder : RecordBuilder.AbstractStringBuilder<NBTBase, NBTTagCompound>(NbtOps) {

    override fun initBuilder(): NBTTagCompound {
        return NBTTagCompound()
    }

    override fun append(
        key: String,
        value: NBTBase,
        builder: NBTTagCompound
    ): NBTTagCompound {
        builder.setTag(key, value)
        return builder
    }

    override fun build(
        builder: NBTTagCompound,
        prefix: NBTBase?
    ): DataResult<NBTBase> {
        if (prefix == null || prefix is NBTTagEnd) {
            return DataResult.success(builder)
        } else if (prefix !is NBTTagCompound) {
            return DataResult.error("mergeToMap called with not a map: $prefix", prefix)
        }

        val copy = prefix.copy() as NBTTagCompound
        for (key in builder.keySet) {
            val value = builder.getTag(key)
            if (value != null) {
                copy.setTag(key, value)
            }
        }

        return DataResult.success(copy)
    }

}

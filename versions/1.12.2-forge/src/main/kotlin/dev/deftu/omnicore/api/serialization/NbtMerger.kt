package dev.deftu.omnicore.api.serialization

import dev.deftu.omnicore.api.nbt.length
import it.unimi.dsi.fastutil.bytes.ByteArrayList
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.longs.LongArrayList
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagByte
import net.minecraft.nbt.NBTTagByteArray
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagLong

internal interface NbtMerger {
    val result: NBTBase

    fun merge(base: NBTBase): NbtMerger

    fun merge(iterable: Iterable<NBTBase>): NbtMerger {
        var current = this
        for (element in iterable) {
            current = current.merge(element)
        }

        return current
    }
}

internal class NbtCompoundListMerger(initial: NBTTagList = NBTTagList()) : NbtMerger {
    private val list = NBTTagList()

    override val result: NBTBase
        get() = list

    constructor(list: IntArrayList) : this(NBTTagList().apply {
        for (item in list) {
            this.appendTag(NBTTagInt(item.toInt()))
        }
    })

    constructor(list: ByteArrayList) : this(NBTTagList().apply {
        for (item in list) {
            this.appendTag(NBTTagByte(item.toByte()))
        }
    })

    constructor(list: LongArrayList) : this(NBTTagList().apply {
        for (item in list) {
            this.appendTag(NBTTagLong(item.toLong()))
        }
    })

    init {
//        for (item in initial) {
//            this.list.appendTag(item)
//        }

        for (i in 0 until initial.length) {
            this.list.appendTag(initial.get(i))
        }
    }

    override fun merge(base: NBTBase): NbtMerger {
        this.list.appendTag(base)
        return this
    }
}

internal class NbtByteArrayMerger(private val bytes: ByteArray) : NbtMerger {
    private val list = ByteArrayList()

    override val result: NBTBase
        get() = NBTTagByteArray(list.toByteArray())

    override fun merge(base: NBTBase): NbtMerger {
        if (base !is NBTTagByte) {
            return NbtCompoundListMerger(this.list).merge(base)
        }

        this.list.add(base.byte)
        return this
    }
}

internal class NbtIntArrayMerger(private val ints: IntArray) : NbtMerger {
    private val list = IntArrayList()

    override val result: NBTBase
        get() = NBTTagList().apply {
            for (item in list) {
                this.appendTag(NBTTagInt(item))
            }
        }

    override fun merge(base: NBTBase): NbtMerger {
        if (base !is NBTTagInt) {
            return NbtCompoundListMerger(this.list).merge(base)
        }

        this.list.add(base.int)
        return this
    }
}

internal class NbtLongArrayMerger(private val longs: LongArray) : NbtMerger {
    private val list = LongArrayList()

    override val result: NBTBase
        get() = NBTTagList().apply {
            for (item in list) {
                this.appendTag(NBTTagLong(item))
            }
        }

    override fun merge(base: NBTBase): NbtMerger {
        if (base !is NBTTagLong) {
            return NbtCompoundListMerger(this.list).merge(base)
        }

        this.list.add(base.long)
        return this
    }
}

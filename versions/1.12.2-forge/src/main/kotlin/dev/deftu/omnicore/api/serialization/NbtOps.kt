package dev.deftu.omnicore.api.serialization

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import dev.deftu.omnicore.api.nbt.NbtType
import dev.deftu.omnicore.api.nbt.length
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagByte
import net.minecraft.nbt.NBTTagByteArray
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagDouble
import net.minecraft.nbt.NBTTagEnd
import net.minecraft.nbt.NBTTagFloat
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagIntArray
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagLong
import net.minecraft.nbt.NBTTagShort
import net.minecraft.nbt.NBTTagString
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.Optional
import java.util.stream.Stream

//#if MC >= 1.12.2
import net.minecraft.nbt.NBTTagLongArray
//#endif

public object NbtOps : DynamicOps<NBTBase> {

    private val EMPTY: NBTTagEnd by lazy {
        val create = NBTBase::class.java.getDeclaredMethod("create", Int::class.java)
        create.isAccessible = true
        create.invoke(null, NbtType.END.id) as NBTTagEnd
    }

    //#if MC >= 1.12.2
    private val NBTTagLongArray.longArray: LongArray
        get() {
            val data = NBTTagLongArray::class.java.getDeclaredField("data")
            data.isAccessible = true
            return data.get(this) as LongArray
        }
    //#endif

    @JvmStatic
    public fun get(): DynamicOps<NBTBase> {
        return NbtOps
    }

    override fun empty(): NBTTagEnd {
        return EMPTY
    }

    @Suppress("UNCHECKED_CAST")
    override fun <U> convertTo(otherOps: DynamicOps<U?>, input: NBTBase): U {
        return when (input) {
            is NBTTagEnd -> otherOps.empty()
            is NBTTagByte -> otherOps.createByte(input.byte)
            is NBTTagShort -> otherOps.createShort(input.short)
            is NBTTagInt -> otherOps.createInt(input.int)
            is NBTTagLong -> otherOps.createLong(input.long)
            is NBTTagFloat -> otherOps.createFloat(input.float)
            is NBTTagDouble -> otherOps.createDouble(input.double)
            is NBTTagByteArray -> otherOps.createByteList(ByteBuffer.wrap(input.byteArray))
            is NBTTagString -> otherOps.createString(input.string)
            is NBTTagList -> this.convertList(otherOps, input)
            is NBTTagCompound -> this.convertMap(otherOps, input)
            is NBTTagIntArray -> otherOps.createIntList(Arrays.stream(input.intArray))
            //#if MC >= 1.12.2
            is NBTTagLongArray -> otherOps.createLongList(Arrays.stream(input.longArray))
            //#endif
            else -> throw IllegalArgumentException("Unknown NBT type: ${input::class.java.name}")
        } as U
    }

    override fun getNumberValue(input: NBTBase): DataResult<Number> {
        return when (input) {
            is NBTTagByte -> DataResult.success(input.byte)
            is NBTTagShort -> DataResult.success(input.short)
            is NBTTagInt -> DataResult.success(input.int)
            is NBTTagLong -> DataResult.success(input.long)
            is NBTTagFloat -> DataResult.success(input.float)
            is NBTTagDouble -> DataResult.success(input.double)
            else -> DataResult.error("Not a number: ${input::class.java.name}")
        }
    }

    override fun createNumeric(i: Number): NBTBase {
        return NBTTagDouble(i.toDouble())
    }

    override fun getStringValue(input: NBTBase): DataResult<String> {
        return if (input is NBTTagString) {
            DataResult.success(input.string)
        } else {
            DataResult.error("Not a string: ${input::class.java.name}")
        }
    }

    override fun createString(value: String): NBTBase {
        return NBTTagString(value)
    }

    override fun mergeToList(
        list: NBTBase,
        value: NBTBase
    ): DataResult<NBTBase> {
        return createMerger(list).map { merger ->
            DataResult.success(merger.merge(value).result)
        }.orElseGet {
            DataResult.error("mergeToList called with not a list: $list", list)
        }
    }

    override fun mergeToMap(
        map: NBTBase,
        key: NBTBase,
        value: NBTBase
    ): DataResult<NBTBase?> {
        if (map !is NBTTagCompound && map !is NBTTagEnd) {
            return DataResult.error("mergeToMap called with not a map: $map", map)
        } else if (key !is NBTTagString) {
            return DataResult.error("key is not a string: $key", map)
        }

        val key = key.string
        val copy = if (map is NBTTagCompound) {
            map.copy() as NBTTagCompound
        } else {
            NBTTagCompound()
        }

        copy.setTag(key, value)
        return DataResult.success(copy)
    }

    override fun getMapValues(input: NBTBase): DataResult<Stream<Pair<NBTBase, NBTBase>>> {
        if (input !is NBTTagCompound) {
            return DataResult.error("Not a map: ${input::class.java.name}")
        }

        val map = mutableMapOf<NBTBase, NBTBase>()
        for (key in input.keySet) {
            val value = input.getTag(key)
            if (value != null) {
                map[NBTTagString(key)] = value
            }
        }

        return DataResult.success(map.entries.stream().map { entry ->
            Pair.of(entry.key, entry.value)
        })
    }

    override fun createMap(stream: Stream<Pair<NBTBase, NBTBase>>): NBTBase {
        val compound = NBTTagCompound()
        for (pair in stream) {
            val key = pair.first
            val value = pair.second
            if (key is NBTTagString) {
                compound.setTag(key.string, value)
            } else {
                throw UnsupportedOperationException("Cannot create map with non-string key: $key")
            }
        }

        return compound
    }

    override fun getStream(input: NBTBase): DataResult<Stream<NBTBase>> {
        if (input !is NBTTagList) {
            return DataResult.error("Not a list: ${input::class.java.name}")
        }

        val newList = mutableListOf<NBTBase>()
        for (i in 0 until input.length) {
            val item = input.get(i)
            if (item != null) {
                newList.add(item)
            }
        }

        return DataResult.success(newList.stream())
    }

    override fun createList(input: Stream<NBTBase>): NBTBase {
        val list = NBTTagList()
        for (item in input) {
            list.appendTag(item)
        }

        return list
    }

    override fun remove(input: NBTBase, key: String): NBTBase {
        if (input !is NBTTagCompound) {
            return input
        }

        val copy = input.copy() as NBTTagCompound
        copy.removeTag(key)
        return copy
    }

    private fun createMerger(base: NBTBase): Optional<NbtMerger> {
        if (base is NBTTagEnd) {
            return Optional.of(NbtCompoundListMerger())
        }

        return when (base) {
            is NBTTagList -> {
                if (base.isEmpty) {
                    Optional.of(NbtCompoundListMerger())
                } else {
                    Optional.of(NbtCompoundListMerger(base))
                }
            }

            is NBTTagByteArray -> {
                if (base.isEmpty) {
                    Optional.of(NbtCompoundListMerger())
                } else {
                    Optional.of(NbtByteArrayMerger(base.byteArray))
                }
            }

            is NBTTagIntArray -> {
                if (base.isEmpty) {
                    Optional.of(NbtCompoundListMerger())
                } else {
                    Optional.of(NbtIntArrayMerger(base.intArray))
                }
            }

            //#if MC >= 1.12.2
            is NBTTagLongArray -> {
                if (base.isEmpty) {
                    Optional.of(NbtCompoundListMerger())
                } else {
                    Optional.of(NbtLongArrayMerger(base.longArray))
                }
            }
            //#endif

            else -> Optional.empty()
        }
    }

}

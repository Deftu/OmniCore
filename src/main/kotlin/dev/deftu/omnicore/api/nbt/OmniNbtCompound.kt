@file:JvmName("OmniNbtCompound")

package dev.deftu.omnicore.api.nbt

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtAccounter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.jvm.optionals.getOrNull

public fun CompoundTag.contains(key: String, type: NbtType): Boolean {
    //#if MC >= 1.21.5
    return get(key)?.id == type.id
    //#else
    //$$ return contains(key, type.id.toInt())
    //#endif
}

public fun CompoundTag.byteOrNull(key: String): Byte? {
    //#if MC >= 1.21.5
    return getByte(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.BYTE)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getByte(key)
    //#endif
}

public fun CompoundTag.byteOrDefault(key: String, default: Byte): Byte {
    //#if MC >= 1.21.5
    return getByteOr(key, default)
    //#else
    //$$ return byteOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.byteOrThrow(key: String): Byte {
    return byteOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.BYTE)
}

public fun CompoundTag.shortOrNull(key: String): Short? {
    //#if MC >= 1.21.5
    return getShort(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.SHORT)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getShort(key)
    //#endif
}

public fun CompoundTag.shortOrDefault(key: String, default: Short): Short {
    //#if MC >= 1.21.5
    return getShortOr(key, default)
    //#else
    //$$ return shortOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.shortOrThrow(key: String): Short {
    return shortOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.SHORT)
}

public fun CompoundTag.intOrNull(key: String): Int? {
    //#if MC >= 1.21.5
    return getInt(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.INT)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getInt(key)
    //#endif
}

public fun CompoundTag.intOrDefault(key: String, default: Int): Int {
    //#if MC >= 1.21.5
    return getIntOr(key, default)
    //#else
    //$$ return intOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.intOrThrow(key: String): Int {
    return intOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.INT)
}

public fun CompoundTag.longOrNull(key: String): Long? {
    //#if MC >= 1.21.5
    return getLong(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.LONG)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getLong(key)
    //#endif
}

public fun CompoundTag.longOrDefault(key: String, default: Long): Long {
    //#if MC >= 1.21.5
    return getLongOr(key, default)
    //#else
    //$$ return longOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.longOrThrow(key: String): Long {
    return longOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.LONG)
}

public fun CompoundTag.floatOrNull(key: String): Float? {
    //#if MC >= 1.21.5
    return getFloat(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.FLOAT)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getFloat(key)
    //#endif
}

public fun CompoundTag.floatOrDefault(key: String, default: Float): Float {
    //#if MC >= 1.21.5
    return getFloatOr(key, default)
    //#else
    //$$ return floatOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.floatOrThrow(key: String): Float {
    return floatOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.FLOAT)
}

public fun CompoundTag.doubleOrNull(key: String): Double? {
    //#if MC >= 1.21.5
    return getDouble(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.DOUBLE)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getDouble(key)
    //#endif
}

public fun CompoundTag.doubleOrDefault(key: String, default: Double): Double {
    //#if MC >= 1.21.5
    return getDoubleOr(key, default)
    //#else
    //$$ return doubleOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.doubleOrThrow(key: String): Double {
    return doubleOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.DOUBLE)
}

public fun CompoundTag.byteArrayOrNull(key: String): ByteArray? {
    //#if MC >= 1.21.5
    return getByteArray(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.BYTE_ARRAY)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getByteArray(key)
    //#endif
}

public fun CompoundTag.byteArrayOrDefault(key: String, default: ByteArray): ByteArray {
    //#if MC >= 1.21.5
    return getByteArray(key).orElse(default)
    //#else
    //$$ return byteArrayOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.byteArrayOrThrow(key: String): ByteArray {
    return byteArrayOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.BYTE_ARRAY)
}

public fun CompoundTag.stringOrNull(key: String): String? {
    //#if MC >= 1.21.5
    return getString(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.STRING)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getString(key)
    //#endif
}

public fun CompoundTag.stringOrDefault(key: String, default: String): String {
    //#if MC >= 1.21.5
    return getStringOr(key, default)
    //#else
    //$$ return stringOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.stringOrThrow(key: String): String {
    return stringOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.STRING)
}

public fun CompoundTag.listOrNull(key: String, type: NbtType): ListTag? {
    //#if MC >= 1.21.5
    return getList(key).orElse(null)
    //#else
    //$$ if (!contains(key, NbtType.LIST)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getList(key, type.id.toInt())
    //#endif
}

public fun CompoundTag.listOrDefault(key: String, type: NbtType, default: ListTag): ListTag {
    //#if MC >= 1.21.5
    return getList(key).orElse(default)
    //#else
    //$$ return listOrNull(key,  type) ?: default
    //#endif
}

public fun CompoundTag.listOrThrow(key: String, type: NbtType): ListTag {
    return listOrNull(key,  type) ?: throw NoSuchNbtElementException(key, NbtType.LIST)
}

public fun CompoundTag.compoundOrNull(key: String): CompoundTag? {
    //#if MC >= 1.21.5
    return getCompound(key).orElse(null)
    //#else
    //$$ if (!contains(key, NbtType.COMPOUND)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getCompound(key)
    //#endif
}

public fun CompoundTag.compoundOrDefault(key: String, default: CompoundTag): CompoundTag {
    //#if MC >= 1.21.5
    return getCompound(key).orElse(default)
    //#else
    //$$ return compoundOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.compoundOrThrow(key: String): CompoundTag {
    return compoundOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.COMPOUND)
}

public fun CompoundTag.intArrayOrNull(key: String): IntArray? {
    //#if MC >= 1.21.5
    return getIntArray(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.INT_ARRAY)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getIntArray(key)
    //#endif
}

public fun CompoundTag.intArrayOrDefault(key: String, default: IntArray): IntArray {
    //#if MC >= 1.21.5
    return getIntArray(key).orElse(default)
    //#else
    //$$ return intArrayOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.intArrayOrThrow(key: String): IntArray {
    return intArrayOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.INT_ARRAY)
}

//#if MC >= 1.16.5
public fun CompoundTag.longArrayOrNull(key: String): LongArray? {
    //#if MC >= 1.21.5
    return getLongArray(key).getOrNull()
    //#else
    //$$ if (!contains(key, NbtType.LONG_ARRAY)) {
    //$$     return null
    //$$ }
    //$$
    //$$ return getLongArray(key)
    //#endif
}

public fun CompoundTag.longArrayOrDefault(key: String, default: LongArray): LongArray {
    //#if MC >= 1.21.5
    return getLongArray(key).orElse(default)
    //#else
    //$$ return longArrayOrNull(key) ?: default
    //#endif
}

public fun CompoundTag.longArrayOrThrow(key: String): LongArray {
    return longArrayOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.LONG_ARRAY)
}
//#endif

public fun CompoundTag.putIfAbsent(key: String, value: CompoundTag) {
    if (!contains(key)) {
        put(key, value)
    }
}

public fun CompoundTag.deepCopy(): CompoundTag {
    return copy() as CompoundTag
}

@JvmName("merge")
public fun CompoundTag.mergeWith(other: CompoundTag): CompoundTag {
    for (key in other.keySet()) {
        val otherValue = other.get(key)
        val thisValue = this.get(key)

        if (thisValue is CompoundTag && otherValue is CompoundTag) {
            thisValue.mergeWith(otherValue)
        } else if (otherValue != null) {
            this.put(key, otherValue)
        }
    }

    return this
}

public fun CompoundTag.findByPath(path: String): Tag? {
    var current: Tag? = this
    for (part in path.split('.')) {
        current = if (current is CompoundTag) {
            current.get(part)
        } else {
            return null
        }
    }

    return current
}

public fun CompoundTag.encodeToByteArray(): ByteArray {
    return ByteArrayOutputStream().apply {
        NbtIo.writeCompressed(this@encodeToByteArray, this)
    }.toByteArray()
}

public fun CompoundTag.writeTo(output: OutputStream) {
    NbtIo.writeCompressed(this, output)
}

@JvmName("decodeFromByteArray")
public fun ByteArray.decodeToNbtCompound(): CompoundTag {
    return NbtIo.readCompressed(
        this.inputStream(),
        //#if MC >= 1.20.4
        NbtAccounter.unlimitedHeap()
        //#endif
    )
}

@JvmName("readFrom")
public fun InputStream.readNbtCompound(): CompoundTag {
    return NbtIo.readCompressed(
        this,
        //#if MC >= 1.20.4
        NbtAccounter.unlimitedHeap()
        //#endif
    )
}

public fun CompoundTag.prettify(indent: Int = 0): String {
    val indentStr = " ".repeat(indent)
    val sb = StringBuilder("{\n")
    for (key in this.keySet()) {
        val value = this.get(key)
        sb.append(indentStr).append("  ").append(key).append(": ")
        if (value is CompoundTag) {
            sb.append(value.prettify(indent + 2))
        } else {
            sb.append(value.toString())
        }
        sb.append("\n")
    }

    sb.append(indentStr).append("}")
    return sb.toString()
}

@file:JvmName("OmniNbtCompound")

package dev.deftu.omnicore.api.nbt

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtSizeTracker
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.jvm.optionals.getOrNull

public fun NbtCompound.contains(key: String, type: NbtType): Boolean {
    //#if MC >= 1.21.5
    return get(key)?.type == type.id
    //#else
    //$$ return contains(key, type.id.toInt())
    //#endif
}

public fun NbtCompound.byteOrNull(key: String): Byte? {
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

public fun NbtCompound.byteOrDefault(key: String, default: Byte): Byte {
    //#if MC >= 1.21.5
    return getByte(key, default)
    //#else
    //$$ return byteOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.byteOrThrow(key: String): Byte {
    return byteOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.BYTE)
}

public fun NbtCompound.shortOrNull(key: String): Short? {
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

public fun NbtCompound.shortOrDefault(key: String, default: Short): Short {
    //#if MC >= 1.21.5
    return getShort(key, default)
    //#else
    //$$ return shortOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.shortOrThrow(key: String): Short {
    return shortOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.SHORT)
}

public fun NbtCompound.intOrNull(key: String): Int? {
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

public fun NbtCompound.intOrDefault(key: String, default: Int): Int {
    //#if MC >= 1.21.5
    return getInt(key, default)
    //#else
    //$$ return intOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.intOrThrow(key: String): Int {
    return intOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.INT)
}

public fun NbtCompound.longOrNull(key: String): Long? {
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

public fun NbtCompound.longOrDefault(key: String, default: Long): Long {
    //#if MC >= 1.21.5
    return getLong(key, default)
    //#else
    //$$ return longOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.longOrThrow(key: String): Long {
    return longOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.LONG)
}

public fun NbtCompound.floatOrNull(key: String): Float? {
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

public fun NbtCompound.floatOrDefault(key: String, default: Float): Float {
    //#if MC >= 1.21.5
    return getFloat(key, default)
    //#else
    //$$ return floatOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.floatOrThrow(key: String): Float {
    return floatOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.FLOAT)
}

public fun NbtCompound.doubleOrNull(key: String): Double? {
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

public fun NbtCompound.doubleOrDefault(key: String, default: Double): Double {
    //#if MC >= 1.21.5
    return getDouble(key, default)
    //#else
    //$$ return doubleOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.doubleOrThrow(key: String): Double {
    return doubleOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.DOUBLE)
}

public fun NbtCompound.byteArrayOrNull(key: String): ByteArray? {
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

public fun NbtCompound.byteArrayOrDefault(key: String, default: ByteArray): ByteArray {
    //#if MC >= 1.21.5
    return getByteArray(key).orElse(default)
    //#else
    //$$ return byteArrayOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.byteArrayOrThrow(key: String): ByteArray {
    return byteArrayOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.BYTE_ARRAY)
}

public fun NbtCompound.stringOrNull(key: String): String? {
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

public fun NbtCompound.stringOrDefault(key: String, default: String): String {
    //#if MC >= 1.21.5
    return getString(key, default)
    //#else
    //$$ return stringOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.stringOrThrow(key: String): String {
    return stringOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.STRING)
}

public fun NbtCompound.listOrNull(key: String, type: NbtType): NbtList? {
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

public fun NbtCompound.listOrDefault(key: String, type: NbtType, default: NbtList): NbtList {
    //#if MC >= 1.21.5
    return getList(key).orElse(default)
    //#else
    //$$ return listOrNull(key,  type) ?: default
    //#endif
}

public fun NbtCompound.listOrThrow(key: String, type: NbtType): NbtList {
    return listOrNull(key,  type) ?: throw NoSuchNbtElementException(key, NbtType.LIST)
}

public fun NbtCompound.compoundOrNull(key: String): NbtCompound? {
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

public fun NbtCompound.compoundOrDefault(key: String, default: NbtCompound): NbtCompound {
    //#if MC >= 1.21.5
    return getCompound(key).orElse(default)
    //#else
    //$$ return compoundOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.compoundOrThrow(key: String): NbtCompound {
    return compoundOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.COMPOUND)
}

public fun NbtCompound.intArrayOrNull(key: String): IntArray? {
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

public fun NbtCompound.intArrayOrDefault(key: String, default: IntArray): IntArray {
    //#if MC >= 1.21.5
    return getIntArray(key).orElse(default)
    //#else
    //$$ return intArrayOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.intArrayOrThrow(key: String): IntArray {
    return intArrayOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.INT_ARRAY)
}

//#if MC >= 1.16.5
public fun NbtCompound.longArrayOrNull(key: String): LongArray? {
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

public fun NbtCompound.longArrayOrDefault(key: String, default: LongArray): LongArray {
    //#if MC >= 1.21.5
    return getLongArray(key).orElse(default)
    //#else
    //$$ return longArrayOrNull(key) ?: default
    //#endif
}

public fun NbtCompound.longArrayOrThrow(key: String): LongArray {
    return longArrayOrNull(key) ?: throw NoSuchNbtElementException(key, NbtType.LONG_ARRAY)
}
//#endif

public fun NbtCompound.putIfAbsent(key: String, value: NbtCompound) {
    if (!contains(key)) {
        put(key, value)
    }
}

public fun NbtCompound.deepCopy(): NbtCompound {
    return copy() as NbtCompound
}

@JvmName("merge")
public fun NbtCompound.mergeWith(other: NbtCompound): NbtCompound {
    for (key in other.keys) {
        val otherValue = other.get(key)
        val thisValue = this.get(key)

        if (thisValue is NbtCompound && otherValue is NbtCompound) {
            thisValue.mergeWith(otherValue)
        } else if (otherValue != null) {
            this.put(key, otherValue)
        }
    }

    return this
}

public fun NbtCompound.findByPath(path: String): NbtElement? {
    var current: NbtElement? = this
    for (part in path.split('.')) {
        current = if (current is NbtCompound) {
            current.get(part)
        } else {
            return null
        }
    }

    return current
}

public fun NbtCompound.encodeToByteArray(): ByteArray {
    return ByteArrayOutputStream().apply {
        NbtIo.writeCompressed(this@encodeToByteArray, this)
    }.toByteArray()
}

public fun NbtCompound.writeTo(output: OutputStream) {
    NbtIo.writeCompressed(this, output)
}

@JvmName("decodeFromByteArray")
public fun ByteArray.decodeToNbtCompound(): NbtCompound {
    return NbtIo.readCompressed(
        this.inputStream(),
        //#if MC >= 1.20.4
        NbtSizeTracker.ofUnlimitedBytes()
        //#endif
    )
}

@JvmName("readFrom")
public fun InputStream.readNbtCompound(): NbtCompound {
    return NbtIo.readCompressed(
        this,
        //#if MC >= 1.20.4
        NbtSizeTracker.ofUnlimitedBytes()
        //#endif
    )
}

public fun NbtCompound.prettify(indent: Int = 0): String {
    val indentStr = " ".repeat(indent)
    val sb = StringBuilder("{\n")
    for (key in this.keys) {
        val value = this.get(key)
        sb.append(indentStr).append("  ").append(key).append(": ")
        if (value is NbtCompound) {
            sb.append(value.prettify(indent + 2))
        } else {
            sb.append(value.toString())
        }
        sb.append("\n")
    }

    sb.append(indentStr).append("}")
    return sb.toString()
}

package dev.deftu.omnicore.common

import dev.deftu.omnicore.api.annotations.VersionedAbove
import net.minecraft.nbt.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

//#if MC <= 1.12.2
//$$ import java.lang.invoke.MethodHandles
//$$ import java.lang.invoke.MethodType
//#endif

public object OmniNbt {

    //#if MC <= 1.12.2
    //$$ private val implLookup by lazy {
    //$$     val field = MethodHandles.Lookup::class.java.getDeclaredField("IMPL_LOOKUP")
    //$$     field.isAccessible = true
    //$$     field.get(null) as MethodHandles.Lookup
    //$$ }
    //#endif

    public const val TYPE_END: Int = 0
    public const val TYPE_BYTE: Int = 1
    public const val TYPE_SHORT: Int = 2
    public const val TYPE_INT: Int = 3
    public const val TYPE_LONG: Int = 4
    public const val TYPE_FLOAT: Int = 5
    public const val TYPE_DOUBLE: Int = 6
    public const val TYPE_BYTE_ARRAY: Int = 7
    public const val TYPE_STRING: Int = 8
    public const val TYPE_LIST: Int = 9
    public const val TYPE_COMPOUND: Int = 10
    public const val TYPE_INT_ARRAY: Int = 11
    public const val TYPE_LONG_ARRAY: Int = 12
    public const val TYPE_NUMBER: Int = 99

    public const val MAX_DEPTH: Int = 512

    @JvmStatic
    public fun parseCompound(value: String): NbtCompound {
        //#if MC >= 1.21.5
        return StringNbtReader.readCompound(value)
        //#elseif MC >= 1.16.5
        //$$ return TagParser(StringReader(value)).readStruct()
        //#else
        //$$ return JsonToNBT.getTagFromJson(value)
        //#endif
    }

    @JvmStatic
    public fun parseElement(value: String): NbtElement {
        //#if MC >= 1.21.5
        return StringNbtReader.fromOps(NbtOps.INSTANCE).read(value)
        //#elseif MC >= 1.16.5
        //$$ return TagParser(StringReader(value)).readValue()
        //#else
        //$$ return try {
        //$$     val constructorHandle = MethodHandles.lookup().findConstructor(NBTBase::class.java, MethodType.methodType(Void.TYPE, String::class.java))
        //$$     val instance = constructorHandle.invokeExact(value)
        //$$
        //$$     val readValueHandle = implLookup.findVirtual(NBTBase::class.java, "readValue", MethodType.methodType(NBTBase::class.java))
        //$$     readValueHandle.invokeExact(instance) as NBTBase
        //$$ } catch (t: Throwable) {
        //$$     throw IllegalArgumentException("Invalid NBT value: $value", t)
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun getTypeName(type: Int): String {
        return when (type) {
            TYPE_END -> "End"
            TYPE_BYTE -> "Byte"
            TYPE_SHORT -> "Short"
            TYPE_INT -> "Int"
            TYPE_LONG -> "Long"
            TYPE_FLOAT -> "Float"
            TYPE_DOUBLE -> "Double"
            TYPE_BYTE_ARRAY -> "ByteArray"
            TYPE_STRING -> "String"
            TYPE_LIST -> "List"
            TYPE_COMPOUND -> "Compound"
            TYPE_INT_ARRAY -> "IntArray"
            TYPE_LONG_ARRAY -> "LongArray"
            else -> "Unknown($type)"
        }
    }

    public object Compound {

        @JvmStatic
        public fun getByteOrDefault(compound: NbtCompound, key: String, default: Byte): Byte {
            //#if MC >= 1.21.5
            return compound.getByte(key, default)
            //#else
            //$$ return if (compound.contains(key, TYPE_BYTE)) {
            //$$     compound.getByte(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getShortOrDefault(compound: NbtCompound, key: String, default: Short): Short {
            //#if MC >= 1.21.5
            return compound.getShort(key, default)
            //#else
            //$$ return if (compound.contains(key, TYPE_SHORT)) {
            //$$     compound.getShort(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getIntOrDefault(compound: NbtCompound, key: String, default: Int): Int {
            //#if MC >= 1.21.5
            return compound.getInt(key, default)
            //#else
            //$$ return if (compound.contains(key, TYPE_INT)) {
            //$$     compound.getInt(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getLongOrDefault(compound: NbtCompound, key: String, default: Long): Long {
            //#if MC >= 1.21.5
            return compound.getLong(key, default)
            //#else
            //$$ return if (compound.contains(key, TYPE_LONG)) {
            //$$     compound.getLong(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getFloatOrDefault(compound: NbtCompound, key: String, default: Float): Float {
            //#if MC >= 1.21.5
            return compound.getFloat(key, default)
            //#else
            //$$ return if (compound.contains(key, TYPE_FLOAT)) {
            //$$     compound.getFloat(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getDoubleOrDefault(compound: NbtCompound, key: String, default: Double): Double {
            //#if MC >= 1.21.5
            return compound.getDouble(key, default)
            //#else
            //$$ return if (compound.contains(key, TYPE_DOUBLE)) {
            //$$     compound.getDouble(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getByteArrayOrDefault(compound: NbtCompound, key: String, default: ByteArray): ByteArray {
            //#if MC >= 1.21.5
            return compound.getByteArray(key).orElse(default)
            //#else
            //$$ return if (compound.contains(key, TYPE_BYTE_ARRAY)) {
            //$$     compound.getByteArray(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getStringOrDefault(compound: NbtCompound, key: String, default: String): String {
            //#if MC >= 1.21.5
            return compound.getString(key, default)
            //#else
            //$$ return if (compound.contains(key, TYPE_STRING)) {
            //$$     compound.getString(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getListOrDefault(compound: NbtCompound, key: String, default: NbtList): NbtList {
            //#if MC >= 1.21.5
            return compound.getList(key).orElse(default)
            //#else
            //$$ return if (compound.contains(key, TYPE_LIST)) {
            //$$     compound.getList(key, TYPE_COMPOUND)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getCompoundOrDefault(compound: NbtCompound, key: String, default: NbtCompound): NbtCompound {
            //#if MC >= 1.21.5
            return compound.getCompound(key).orElse(default)
            //#else
            //$$ return if (compound.contains(key, TYPE_COMPOUND)) {
            //$$     compound.getCompound(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        @JvmStatic
        public fun getIntArrayOrDefault(compound: NbtCompound, key: String, default: IntArray): IntArray {
            //#if MC >= 1.21.5
            return compound.getIntArray(key).orElse(default)
            //#else
            //$$ return if (compound.contains(key, TYPE_INT_ARRAY)) {
            //$$     compound.getIntArray(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }

        //#if MC >= 1.16.5
        @JvmStatic
        @VersionedAbove("1.16.5")
        public fun getLongArrayOrDefault(compound: NbtCompound, key: String, default: LongArray): LongArray {
            //#if MC >= 1.21.5
            return compound.getLongArray(key).orElse(default)
            //#else
            //$$ return if (compound.contains(key, TYPE_LONG_ARRAY)) {
            //$$     compound.getLongArray(key)
            //$$ } else {
            //$$     default
            //$$ }
            //#endif
        }
        //#endif

        @JvmStatic
        @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
        public fun <T> getOrDefault(compound: NbtCompound, key: String, default: T): T {
            return when (default) {
                is Byte -> getByteOrDefault(compound, key, default)
                is Short -> getShortOrDefault(compound, key, default)
                is Int -> getIntOrDefault(compound, key, default)
                is Long -> getLongOrDefault(compound, key, default)
                is Float -> getFloatOrDefault(compound, key, default)
                is Double -> getDoubleOrDefault(compound, key, default)
                is ByteArray -> getByteArrayOrDefault(compound, key, default)
                is String -> getStringOrDefault(compound, key, default)
                is NbtList -> getListOrDefault(compound, key, default)
                is NbtCompound -> getCompoundOrDefault(compound, key, default)
                is IntArray -> getIntArrayOrDefault(compound, key, default)
                //#if MC >= 1.16.5
                is LongArray -> getLongArrayOrDefault(compound, key, default)
                //#endif
                else -> throw IllegalArgumentException("Unsupported type: ${default!!::class.java}")
            } as T
        }

        @JvmStatic
        public fun putIfAbsent(compound: NbtCompound, key: String, value: NbtElement) {
            if (!compound.contains(key)) {
                compound.put(key, value)
            }
        }

        @JvmStatic
        public fun deepCopy(compound: NbtCompound): NbtCompound {
            return compound.copy() as NbtCompound
        }

        @JvmStatic
        public fun merge(compound: NbtCompound, other: NbtCompound): NbtCompound {
            for (key in other.keys) {
                val otherValue = other.get(key)
                val thisValue = compound.get(key)

                if (thisValue is NbtCompound && otherValue is NbtCompound) {
                    merge(thisValue, otherValue)
                } else if (otherValue != null) {
                    compound.put(key, otherValue)
                }
            }

            return compound
        }

        @JvmStatic
        public fun getByPath(compound: NbtCompound, path: String): NbtElement? {
            var current: NbtElement? = compound
            for (part in path.split('.')) {
                current = if (current is NbtCompound) {
                    current.get(part)
                } else {
                    return null
                }
            }

            return current
        }

        @JvmStatic
        public fun containsPath(compound: NbtCompound, path: String): Boolean {
            return getByPath(compound, path) != null
        }

        @JvmStatic
        public fun removeByPath(compound: NbtCompound, path: String): Boolean {
            val parts = path.split('.')
            val last = parts.last()
            val parent = getByPath(compound, parts.dropLast(1).joinToString("."))
            return if (parent is NbtCompound && parent.contains(last)) {
                parent.remove(last)
                true
            } else false
        }

        @JvmStatic
        public fun toByteArray(compound: NbtCompound): ByteArray {
            return ByteArrayOutputStream().apply {
                NbtIo.writeCompressed(compound, this)
            }.toByteArray()
        }

        @JvmStatic
        public fun fromByteArray(bytes: ByteArray): NbtCompound {
            return NbtIo.readCompressed(
                bytes.inputStream(),
                //#if MC >= 1.20.4
                NbtSizeTracker.ofUnlimitedBytes()
                //#endif
            )
        }

        @JvmStatic
        public fun writeToStream(compound: NbtCompound, out: OutputStream) {
            NbtIo.writeCompressed(compound, out)
        }

        @JvmStatic
        public fun fromStream(input: InputStream): NbtCompound {
            return NbtIo.readCompressed(
                input,
                //#if MC >= 1.20.4
                NbtSizeTracker.ofUnlimitedBytes()
                //#endif
            )
        }

        @JvmStatic
        public fun toBase64(compound: NbtCompound): String {
            return Base64.getEncoder().encodeToString(toByteArray(compound))
        }

        @JvmStatic
        public fun fromBase64(base64: String): NbtCompound {
            val bytes = Base64.getDecoder().decode(base64)
            return fromByteArray(bytes)
        }

        @JvmStatic
        public fun prettify(compound: NbtCompound, indent: Int = 0): String {
            val indentStr = " ".repeat(indent)
            val sb = StringBuilder("{\n")
            for (key in compound.keys) {
                val value = compound.get(key)
                sb.append(indentStr).append("  ").append(key).append(": ")
                if (value is NbtCompound) {
                    sb.append(prettify(value, indent + 2))
                } else {
                    sb.append(value.toString())
                }
                sb.append("\n")
            }

            sb.append(indentStr).append("}")
            return sb.toString()
        }

        @JvmStatic
        public fun validate(compound: NbtCompound, requiredKeys: Map<String, Int>): Boolean {
            for ((key, expectedType) in requiredKeys) {
                //#if MC >= 1.21.5
                val tag = compound.get(key) ?: return false
                if (tag.type.toInt() != expectedType) {
                    return false
                }
                //#else
                //$$ if (!compound.contains(key, expectedType)) {
                //$$     return false
                //$$ }
                //#endif
            }

            return true
        }

    }

    public object List {

        @JvmStatic
        public fun add(list: NbtList, element: NbtElement) {
            //#if MC >= 1.16.5
            list.add(element)
            //#else
            //$$ list.appendTag(element)
            //#endif
        }

        @JvmStatic
        public fun size(list: NbtList): Int {
            //#if MC >= 1.16.5
            return list.size
            //#else
            //$$ return list.tagCount()
            //#endif
        }

        @JvmStatic
        public fun forEachCompound(list: NbtList, action: (NbtCompound) -> Unit) {
            for (i in 0 until size(list)) {
                val element = list[i]
                if (element is NbtCompound) {
                    action(element)
                }
            }
        }

        @JvmStatic
        public fun getCompoundOrNull(list: NbtList, index: Int): NbtCompound? {
            val element = list[index]
            return if (element is NbtCompound) {
                element
            } else {
                null
            }
        }

        @JvmStatic
        public fun mapCompounds(list: NbtList, transform: (NbtCompound) -> NbtElement): NbtList {
            val result = NbtList()
            forEachCompound(list) { add(result, transform(it)) }
            return result
        }

        @JvmStatic
        public fun filterCompounds(list: NbtList, predicate: (NbtCompound) -> Boolean): NbtList {
            val result = NbtList()
            forEachCompound(list) { if (predicate(it)) add(result, it) }
            return result
        }

        @JvmStatic
        public fun findCompound(list: NbtList, predicate: (NbtCompound) -> Boolean): NbtCompound? {
            for (i in 0 until size(list)) {
                val element = list[i]
                if (element is NbtCompound && predicate(element)) return element
            }

            return null
        }

    }

}

public fun NbtCompound.getByteOrDefault(key: String, default: Byte): Byte {
    return OmniNbt.Compound.getByteOrDefault(this, key, default)
}

public fun NbtCompound.getShortOrDefault(key: String, default: Short): Short {
    return OmniNbt.Compound.getShortOrDefault(this, key, default)
}

public fun NbtCompound.getIntOrDefault(key: String, default: Int): Int {
    return OmniNbt.Compound.getIntOrDefault(this, key, default)
}

public fun NbtCompound.getLongOrDefault(key: String, default: Long): Long {
    return OmniNbt.Compound.getLongOrDefault(this, key, default)
}

public fun NbtCompound.getFloatOrDefault(key: String, default: Float): Float {
    return OmniNbt.Compound.getFloatOrDefault(this, key, default)
}

public fun NbtCompound.getDoubleOrDefault(key: String, default: Double): Double {
    return OmniNbt.Compound.getDoubleOrDefault(this, key, default)
}

public fun NbtCompound.getByteArrayOrDefault(key: String, default: ByteArray): ByteArray {
    return OmniNbt.Compound.getByteArrayOrDefault(this, key, default)
}

public fun NbtCompound.getStringOrDefault(key: String, default: String): String {
    return OmniNbt.Compound.getStringOrDefault(this, key, default)
}

public fun NbtCompound.getListOrDefault(key: String, default: NbtList): NbtList {
    return OmniNbt.Compound.getListOrDefault(this, key, default)
}

public fun NbtCompound.getCompoundOrDefault(key: String, default: NbtCompound): NbtCompound {
    return OmniNbt.Compound.getCompoundOrDefault(this, key, default)
}

public fun NbtCompound.getIntArrayOrDefault(key: String, default: IntArray): IntArray {
    return OmniNbt.Compound.getIntArrayOrDefault(this, key, default)
}

//#if MC >= 1.16.5
@VersionedAbove("1.16.5")
public fun NbtCompound.getLongArrayOrDefault(key: String, default: LongArray): LongArray {
    return OmniNbt.Compound.getLongArrayOrDefault(this, key, default)
}
//#endif

public fun <T> NbtCompound.getOrDefault(key: String, default: T): T {
    return OmniNbt.Compound.getOrDefault(this, key, default)
}

public fun NbtCompound.putIfAbsent(key: String, value: NbtElement) {
    return OmniNbt.Compound.putIfAbsent(this, key, value)
}

public fun NbtCompound.deepCopy(): NbtCompound =
    OmniNbt.Compound.deepCopy(this)

public fun NbtCompound.merge(other: NbtCompound): NbtCompound {
    return OmniNbt.Compound.merge(this, other)
}

public fun NbtCompound.getByPath(path: String): NbtElement? {
    return OmniNbt.Compound.getByPath(this, path)
}

public fun NbtCompound.toByteArray(): ByteArray {
    return OmniNbt.Compound.toByteArray(this)
}

public fun NbtCompound.toBase64(): String {
    return OmniNbt.Compound.toBase64(this)
}

public fun NbtCompound.writeToStream(out: OutputStream) {
    return OmniNbt.Compound.writeToStream(this, out)
}

public fun NbtCompound.prettify(indent: Int = 0): String {
    return OmniNbt.Compound.prettify(this, indent)
}

public fun NbtCompound.validate(requiredKeys: Map<String, Int>): Boolean {
    return OmniNbt.Compound.validate(this, requiredKeys)
}

//#if MC <= 1.12.2
//$$ @VersionedBelow("1.12.2")
//$$ public fun NBTTagList.add(element: NBTBase) {
//$$     return OmniNbt.List.add(this, element)
//$$ }
//$$
//$$ @VersionedBelow("1.12.2")
//$$ public val NBTTagList.size: Int
//$$     get() {
//$$         return OmniNbt.List.size(this)
//$$     }
//#endif

public fun NbtList.forEachCompound(action: (NbtCompound) -> Unit) {
    return OmniNbt.List.forEachCompound(this, action)
}

public fun NbtList.getCompoundOrNull(index: Int): NbtCompound? {
    return OmniNbt.List.getCompoundOrNull(this, index)
}

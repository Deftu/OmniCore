package dev.deftu.omnicore.api

import java.util.UUID

public object OmniUuid {
    @JvmStatic
    public fun fromByteArray(array: ByteArray): UUID {
        require(array.size == 16) { "Byte array must have exactly 16 bytes" }
        val msb = array.take(8).fold(0L) { acc, b -> (acc shl 8) or (b.toLong() and 0xFF) }
        val lsb = array.drop(8).fold(0L) { acc, b -> (acc shl 8) or (b.toLong() and 0xFF) }
        return UUID(msb, lsb)
    }

    @JvmStatic
    public fun toByteArray(mostSignificantBits: Long, leastSignificantBits: Long): ByteArray {
        val array = ByteArray(16)
        for (i in 0..7) array[i] = (mostSignificantBits shr (8 * (7 - i))).toByte()
        for (i in 0..7) array[8 + i] = (leastSignificantBits shr (8 * (7 - i))).toByte()
        return array
    }

    @JvmStatic
    public fun toByteArray(uuid: UUID): ByteArray {
        return toByteArray(uuid.mostSignificantBits, uuid.leastSignificantBits)
    }

    @JvmStatic
    public fun fromIntArray(array: IntArray): UUID {
        if (array.size != 4) {
            throw IllegalArgumentException("Array must have exactly 4 elements")
        }

        return UUID(
            array[0].toLong() shl 32 or (array[1].toLong() and 0xFFFFFFFFL),
            array[2].toLong() shl 32 or (array[3].toLong() and 0xFFFFFFFFL)
        )
    }

    @JvmStatic
    public fun toIntArray(mostSignificantBits: Long, leastSignificantBits: Long): IntArray {
        return intArrayOf(
            (mostSignificantBits shr 32).toInt(),
            mostSignificantBits.toInt(),
            (leastSignificantBits shr 32).toInt(),
            leastSignificantBits.toInt()
        )
    }

    @JvmStatic
    public fun toIntArray(uuid: UUID): IntArray {
        return toIntArray(uuid.mostSignificantBits, uuid.leastSignificantBits)
    }

    @JvmStatic
    public fun fromUndashed(value: String): UUID {
        if (value.indexOf('-') != -1) {
            throw IllegalArgumentException("UUID string must not contain dashes")
        }

        return UUID.fromString(value.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5"));
    }

    @JvmStatic
    public fun toUndashed(uuid: UUID): String {
        return uuid.toString().replace("-", "")
    }
}
package dev.deftu.omnicore.api.nbt

import dev.deftu.omnicore.api.annotations.VersionedAbove

public enum class NbtType(public val id: Byte, typeName: String? = null) {
    END(0),
    BYTE(1),
    SHORT(2),
    INT(3),
    LONG(4),
    FLOAT(5),
    DOUBLE(6),
    BYTE_ARRAY(7),
    STRING(8),
    LIST(9),
    COMPOUND(10),
    INT_ARRAY(11),
    @VersionedAbove("1.16.5") LONG_ARRAY(12),
    NUMBER(99);

    /**
     * END -> End
     * BYTE_ARRAY -> ByteArray
     */
    @Suppress("EnumValuesSoftDeprecate")
    public val typeName: String = typeName ?: id.toInt().let { id ->
        if (id == 99) {
            "Number"
        } else {
            values().first { type ->
                type.id.toInt() == id
            }.name.split('_').joinToString("") { part ->
                part.lowercase().replaceFirstChar { it.uppercase() }
            }
        }
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<NbtType> = values().toList()

        @JvmField
        public val ID_MAPPINGS: Map<Byte, NbtType> = ALL.associateBy(NbtType::id)

        @JvmField
        public val ID_MAPPINGS_INVERSE: Map<NbtType, Byte> = ID_MAPPINGS.entries
            .associate { (key, value) -> value to key }

        public fun from(id: Byte): NbtType {
            return ID_MAPPINGS[id] ?: throw IllegalArgumentException("Unknown NBT type id: $id")
        }
    }
}

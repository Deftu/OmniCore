package dev.deftu.omnicore.api.client.render

import org.lwjgl.opengl.GL13

public enum class OmniTextureUnit(public val id: Int) {
    TEXTURE0(0),
    TEXTURE1(1),
    TEXTURE2(2),
    TEXTURE3(3),
    TEXTURE4(4),
    TEXTURE5(5),
    TEXTURE6(6),
    TEXTURE7(7),
    TEXTURE8(8),
    TEXTURE9(9),
    TEXTURE10(10),
    TEXTURE11(11),
    TEXTURE12(12);

    public val const: Int
        get() = GL13.GL_TEXTURE0 + id

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniTextureUnit> = values().toList()

        @JvmStatic
        public fun from(id: Int): OmniTextureUnit? {
            return ALL.firstOrNull { it.id == id }
        }

        @JvmStatic
        public fun gl(const: Int): OmniTextureUnit? {
            return from(const - GL13.GL_TEXTURE0)
        }
    }
}

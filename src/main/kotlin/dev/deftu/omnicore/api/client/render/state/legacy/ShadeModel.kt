package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.utils.GlConstEnum
import org.lwjgl.opengl.GL11

public enum class ShadeModel(override val const: Int) : GlConstEnum {
    FLAT(GL11.GL_FLAT),
    SMOOTH(GL11.GL_SMOOTH);

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<ShadeModel> = ShadeModel.values().toList()

        @JvmStatic
        public fun findOrNull(const: Int): ShadeModel? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        @JvmStatic
        public fun findOrThrow(const: Int): ShadeModel {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}

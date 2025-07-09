package dev.deftu.omnicore.client.render.state

import com.mojang.blaze3d.opengl.GlStateManager
import org.lwjgl.opengl.GL11

public data class OmniManagedPolygonOffset(
    public val isEnabled: Boolean,
    public val factor: Float,
    public val units: Float,
) {

    public fun activate() {
        if (isEnabled) {
            GlStateManager._enablePolygonOffset()
            GlStateManager._polygonOffset(factor, units)
        } else {
            GlStateManager._disablePolygonOffset()
        }
    }

    public companion object {

        @JvmField
        public val DEFAULT: OmniManagedPolygonOffset = OmniManagedPolygonOffset(false, 0f, 0f)

        @JvmField
        public val DISABLED: OmniManagedPolygonOffset = DEFAULT

        @JvmStatic
        public fun active(): OmniManagedPolygonOffset {
            return OmniManagedPolygonOffset(
                isEnabled = GL11.glIsEnabled(GL11.GL_POLYGON_OFFSET_FILL),
                factor = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_FACTOR),
                units = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_UNITS),
            )
        }

        @JvmStatic
        public fun asEnabled(factor: Float, units: Float): OmniManagedPolygonOffset {
            return OmniManagedPolygonOffset(
                isEnabled = true,
                factor = factor,
                units = units,
            )
        }

        @JvmStatic
        public fun enable(factor: Float, units: Float): OmniManagedPolygonOffset {
            return asEnabled(factor, units).also(OmniManagedPolygonOffset::activate)
        }

        @JvmStatic
        public fun disable(): OmniManagedPolygonOffset {
            return DISABLED.also(OmniManagedPolygonOffset::activate)
        }

    }

}

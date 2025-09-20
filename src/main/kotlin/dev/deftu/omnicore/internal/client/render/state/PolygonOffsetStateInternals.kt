package dev.deftu.omnicore.internal.client.render.state

import com.mojang.blaze3d.opengl.GlStateManager
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11

@ApiStatus.Internal
public object PolygonOffsetStateInternals {
    @JvmStatic
    public val isEnabled: Boolean
        get() = GL11.glIsEnabled(GL11.GL_POLYGON_OFFSET_FILL)

    @JvmStatic
    public val factor: Float
        get() = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_FACTOR)

    @JvmStatic
    public val units: Float
        get() = GL11.glGetFloat(GL11.GL_POLYGON_OFFSET_UNITS)

    @JvmStatic
    public fun enable() {
        GlStateManager._enablePolygonOffset()
    }

    @JvmStatic
    public fun disable() {
        GlStateManager._disablePolygonOffset()
    }

    @JvmStatic
    public fun configure(factor: Float, units: Float) {
        GlStateManager._polygonOffset(factor, units)
    }
}

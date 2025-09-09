package dev.deftu.omnicore.internal.client.render.state

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.render.state.CullFace
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11

@ApiStatus.Internal
public object CullStateInternals {
    @JvmStatic
    public val isEnabled: Boolean
        get() = GL11.glIsEnabled(GL11.GL_CULL_FACE)

    @JvmStatic
    public val mode: CullFace
        get() {
            val mode = GL11.glGetInteger(GL11.GL_CULL_FACE_MODE)
            return CullFace.findOrThrow(mode)
        }

    @JvmStatic
    public fun enable() {
        GlStateManager._enableCull()
    }

    @JvmStatic
    public fun disable() {
        GlStateManager._disableCull()
    }

    @JvmStatic
    public fun face(mode: Int) {
        GL11.glCullFace(mode)
    }
}

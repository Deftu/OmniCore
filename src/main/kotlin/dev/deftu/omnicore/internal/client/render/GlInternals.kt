package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.render.ClearMask
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11

@ApiStatus.Internal
public object GlInternals {
    @JvmStatic
    public fun clear0(bits: Int) {
        //#if MC >= 1.21.5
        GL11.glClear(bits)
        //#else
        //$$ GlStateManager._clear(
        //$$     bits,
        //#if MC >= 1.16.5 && MC < 1.21.2
        //$$ false,
        //#endif
        //$$ )
        //#endif
    }

    @JvmStatic
    public fun clear(mask: ClearMask) {
        clear0(mask.bits)
    }

    @JvmStatic
    public fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        //#if MC >= 1.21.5
        GL11.glClearColor(red, green, blue, alpha)
        //#else
        //$$ GlStateManager._clearColor(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic
    public fun clearDepth(depth: Double) {
        //#if MC >= 1.21.5
        GL11.glClearDepth(depth)
        //#else
        //$$ GlStateManager._clearDepth(depth)
        //#endif
    }

    @JvmStatic
    public fun clearStencil(stencil: Int) {
        //#if MC >= 1.21.5 || MC <= 1.12.2
        GL11.glClearStencil(stencil)
        //#else
        //$$ GlStateManager._clearStencil(stencil)
        //#endif
    }

    @JvmStatic
    public fun depthMask(flag: Boolean) {
        GlStateManager._depthMask(flag)
    }
}

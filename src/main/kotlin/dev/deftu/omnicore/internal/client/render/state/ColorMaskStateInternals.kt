package dev.deftu.omnicore.internal.client.render.state

import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

//#if MC >= 1.21.5
import com.mojang.blaze3d.opengl.GlStateManager
//#elseif MC >= 1.16.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#else
//$$ import net.minecraft.client.renderer.GlStateManager
//#endif

@ApiStatus.Internal
public object ColorMaskStateInternals {
    private val internalBuffer = ByteBuffer.allocateDirect(16)

    /** [[r, g], [b, a]] */
    @JvmStatic
    public val values: Pair<Pair<Boolean, Boolean>, Pair<Boolean, Boolean>>
        get() {
            return internalBuffer.also { buffer ->
                //#if MC >= 1.16.5
                GL11.glGetBooleanv(GL11.GL_COLOR_WRITEMASK, buffer)
                //#else
                //$$ GL11.glGetBoolean(GL11.GL_COLOR_WRITEMASK, buffer)
                //#endif
            }.let { buffer ->
                List(4) {
                    buffer.get(it).toInt() != 0
                }
            }.let { list ->
                (list[0] to list[1]) to (list[2] to list[3])
            }
        }

    @JvmStatic
    public fun configure(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
        //#if MC >= 1.21.5
        GlStateManager._colorMask(red, green, blue, alpha)
        //#elseif MC >= 1.16.5
        //$$ RenderSystem.colorMask(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.colorMask(red, green, blue, alpha)
        //#endif
    }
}

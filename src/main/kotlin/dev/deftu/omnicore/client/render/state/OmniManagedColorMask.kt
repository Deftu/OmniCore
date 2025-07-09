package dev.deftu.omnicore.client.render.state

import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

//#if MC >= 1.21.5
import com.mojang.blaze3d.opengl.GlStateManager
//#elseif MC >= 1.16.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#else
//$$ import net.minecraft.client.renderer.GlStateManager
//#endif

public data class OmniManagedColorMask(
    val red: Boolean,
    val green: Boolean,
    val blue: Boolean,
    val alpha: Boolean
) {

    public fun activate() {
        //#if MC >= 1.21.5
        GlStateManager._colorMask(red, green, blue, alpha)
        //#elseif MC >= 1.16.5
        //$$ RenderSystem.colorMask(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.colorMask(red, green, blue, alpha)
        //#endif
    }

    public companion object {

        @JvmField
        public val DEFAULT: OmniManagedColorMask = OmniManagedColorMask(
            red = true,
            green = true,
            blue = true,
            alpha = true
        )

        @JvmStatic
        public fun active(): OmniManagedColorMask {
            val values = booleans(GL11.GL_COLOR_WRITEMASK, 4)
            return OmniManagedColorMask(
                red = values[0],
                green = values[1],
                blue = values[2],
                alpha = values[3]
            )
        }

        private fun booleans(param: Int, count: Int): List<Boolean> {
            val buffer = ByteBuffer.allocateDirect(16)
            return buffer.also { buffer ->
                //#if MC >= 1.16.5
                GL11.glGetBooleanv(param, buffer)
                //#else
                //$$ GL11.glGetBoolean(param, buffer)
                //#endif
            }.let { buffer ->
                List(count) { i ->
                    buffer[i] != 0.toByte()
                }
            }
        }

    }

}

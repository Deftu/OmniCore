package dev.deftu.omnicore.client.render

import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

public data class ColorMask(
    val red: Boolean,
    val green: Boolean,
    val blue: Boolean,
    val alpha: Boolean
) {

    public companion object {

        public fun active(): ColorMask {
            val values = booleans(GL11.GL_COLOR_WRITEMASK, 4)
            return ColorMask(
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

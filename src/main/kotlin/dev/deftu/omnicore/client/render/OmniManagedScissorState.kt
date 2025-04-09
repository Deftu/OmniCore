package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

//#if MC >= 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#else
import com.mojang.blaze3d.platform.GlStateManager
//#endif

@GameSide(Side.CLIENT)
public data class OmniManagedScissorState(
    public val isEnabled: Boolean,
    public val x: Int,
    public val y: Int,
    public val width: Int,
    public val height: Int
) {

    public fun activate() {
        if (isEnabled) {
            //#if MC >= 1.21.5
            //$$ RenderSystem.SCISSOR_STATE.enable(x, y, width, height)
            //#else
            GlStateManager._enableScissorTest()
            //#endif
        } else {
            //#if MC >= 1.21.5
            //$$ RenderSystem.SCISSOR_STATE.disable()
            //#else
            GlStateManager._disableScissorTest()
            //#endif
        }

        //#if MC <= 1.21.4
        GlStateManager._scissorBox(x, y, width, height)
        //#endif
    }

    public companion object {

        @JvmField
        @GameSide(Side.CLIENT)
        public val DISABLED: OmniManagedScissorState = OmniManagedScissorState(false, 0, 0, 0, 0)

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun active(): OmniManagedScissorState {
            //#if MC >= 1.21.5
            //$$ val vanillaState = RenderSystem.SCISSOR_STATE
            //$$ return OmniManagedScissorState(
            //$$     isEnabled = vanillaState.isEnabled,
            //$$     x = vanillaState.x,
            //$$     y = vanillaState.y,
            //$$     width = vanillaState.width,
            //$$     height = vanillaState.height
            //$$ )
            //#else
            val scissorBounds = ints(GL11.GL_SCISSOR_BOX, 4)
            return OmniManagedScissorState(
                isEnabled = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST),
                x = scissorBounds[0],
                y = scissorBounds[1],
                width = scissorBounds[2],
                height = scissorBounds[3]
            )
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun enable(x: Int, y: Int, width: Int, height: Int) {
            OmniManagedScissorState(
                isEnabled = true,
                x = x,
                y = y,
                width = width,
                height = height
            ).activate()
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun disable() {
            DISABLED.activate()
        }

        //#if MC <= 1.21.4
        private fun ints(param: Int, count: Int): List<Int> {
            val buffer = ByteBuffer.allocateDirect(16 * Int.SIZE_BYTES).asIntBuffer()
            return buffer.also { buffer ->
                //#if MC >= 1.16.5
                GL11.glGetIntegerv(param, buffer)
                //#else
                //$$ GL11.glGetInteger(param, buffer)
                //#endif
            }.let { _ ->
                List(count) { i ->
                    buffer[i]
                }
            }
        }
        //#endif

    }

}

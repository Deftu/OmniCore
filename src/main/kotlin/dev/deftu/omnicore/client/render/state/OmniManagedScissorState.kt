package dev.deftu.omnicore.client.render.state

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

//#if MC >= 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderPass
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

    private var previousState: OmniManagedScissorState? = null

    @JvmOverloads
    public fun activate(withPrevious: Boolean = true) {
        if (withPrevious) {
            this.previousState = active()
        }

        if (isEnabled) {
            //#if MC >= 1.21.5
            //$$ RenderSystem.SCISSOR_STATE.enable(x, y, width, height)
            //#elseif MC >= 1.17.1
            GlStateManager._enableScissorTest()
            //#else
            //$$ GL11.glEnable(GL11.GL_SCISSOR_TEST)
            //#endif
        } else {
            //#if MC >= 1.21.5
            //$$ RenderSystem.SCISSOR_STATE.disable()
            //#elseif MC >= 1.17.1
            GlStateManager._disableScissorTest()
            //#else
            //$$ GL11.glDisable(GL11.GL_SCISSOR_TEST)
            //#endif
        }

        //#if MC <= 1.21.4
        //#if MC >= 1.17.1
        GlStateManager._scissorBox(x, y, width, height)
        //#else
        //$$ GL11.glScissor(x, y, width, height)
        //#endif
        //#endif
    }

    public fun restore() {
        this.previousState?.activate(withPrevious = false)
    }

    //#if MC >= 1.21.5
    //$$ public fun applyTo(renderPass: RenderPass) {
    //$$     if (isEnabled) {
    //$$         renderPass.enableScissor(x, y, width, height)
    //$$     } else {
    //$$         renderPass.disableScissor()
    //$$     }
    //$$ }
    //#endif

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
        public fun asEnabled(
            x: Int,
            y: Int,
            width: Int,
            height: Int
        ): OmniManagedScissorState {
            return OmniManagedScissorState(
                isEnabled = true,
                x = x,
                y = y,
                width = width,
                height = height
            )
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun enable(x: Int, y: Int, width: Int, height: Int): OmniManagedScissorState {
            return asEnabled(x, y, width, height).also(OmniManagedScissorState::activate)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun disable(): OmniManagedScissorState {
            return DISABLED.also(OmniManagedScissorState::activate)
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

package dev.deftu.omnicore.internal.client.render

import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.21.6
import net.minecraft.client.gui.DrawContext
//#elseif MC >= 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#elseif MC >= 1.17.1
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

//#if MC < 1.21.5
//$$ import java.nio.ByteBuffer
//$$ import org.lwjgl.opengl.GL11
//#endif

@ApiStatus.Internal
public object ScissorInternals {
    //#if MC < 1.21.6
    //$$ @JvmStatic
    //$$ public val activeScissorState: OmniRenderingContext.ScissorBox?
    //$$     get() {
            //#if MC >= 1.21.5
            //$$ if (!RenderSystem.SCISSOR_STATE.isEnabled) {
            //$$     return null
            //$$ }
            //$$
            //$$ val state = RenderSystem.SCISSOR_STATE
            //$$ return OmniRenderingContext.ScissorBox(
            //$$     x = state.x,
            //$$     y = state.y,
            //$$     width = state.width,
            //$$     height = state.height
            //$$ )
            //#else
            //$$ if (!GL11.glIsEnabled(GL11.GL_SCISSOR_TEST)) {
            //$$     return null
            //$$ }
            //$$
            //$$ val buffer = ByteBuffer.allocateDirect(16 * Int.SIZE_BYTES).asIntBuffer()
            //$$ val params = buffer.also { buffer ->
                //#if MC >= 1.16.5
                //$$ GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, buffer)
                //#else
                //$$ GL11.glGetInteger(GL11.GL_SCISSOR_BOX, buffer)
                //#endif
            //$$ }.let { _ ->
            //$$     List(4) { buffer[it] }.toIntArray()
            //$$ }
            //$$
            //$$ return OmniRenderingContext.ScissorBox(
            //$$     x = params[0],
            //$$     y = params[1],
            //$$     width = params[2],
            //$$     height = params[3]
            //$$ )
            //#endif
    //$$     }
    //#endif

    @JvmStatic
    public fun applyScissor(
        //#if MC >= 1.21.6
        graphics: DrawContext,
        //#endif
        box: OmniRenderingContext.ScissorBox
    ) {
        //#if MC >= 1.21.6
        graphics.enableScissor(box.x, box.y, box.width, box.height)
        //#elseif MC >= 1.21.5
        //$$ RenderSystem.SCISSOR_STATE.enable(box.x, box.y, box.width, box.height)
        //#elseif MC >= 1.17.1
        //$$ GlStateManager._enableScissorTest()
        //$$ GlStateManager._scissorBox(box.x, box.y, box.width, box.height)
        //#else
        //$$ GL11.glEnable(GL11.GL_SCISSOR_TEST)
        //$$ GL11.glScissor(box.x, box.y, box.width, box.height)
        //#endif
    }

    @JvmStatic
    public fun disableScissor(
        //#if MC >= 1.21.6
        graphics: DrawContext
        //#endif
    ) {
        //#if MC >= 1.21.6
        graphics.disableScissor()
        //#elseif MC >= 1.21.5
        //$$ RenderSystem.SCISSOR_STATE.disable()
        //#elseif MC >= 1.17.1
        //$$ GlStateManager._disableScissorTest()
        //#else
        //$$ GL11.glDisable(GL11.GL_SCISSOR_TEST)
        //#endif
    }
}

package dev.deftu.omnicore.internal.client.render

import dev.deftu.omnicore.api.client.render.ScissorBox
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.21.5
import net.minecraft.client.gui.DrawContext
//#elseif MC >= 1.17.1
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

//#if MC < 1.21.6
//$$ import dev.deftu.omnicore.api.client.render.OmniResolution
//#endif

//#if MC == 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC < 1.21.5
//$$ import java.nio.ByteBuffer
//$$ import org.lwjgl.opengl.GL11
//#endif

@ApiStatus.Internal
public object ScissorInternals {
    //#if MC >= 1.21.5
    @JvmStatic
    public var activeScissorState: ScissorBox? = null
        private set
    //#endif

    //#if MC < 1.21.5
    //$$ @JvmStatic
    //$$ public val activeScissorState: ScissorBox?
    //$$     get() {
    //$$         if (!GL11.glIsEnabled(GL11.GL_SCISSOR_TEST)) {
    //$$             return null
    //$$         }
    //$$
    //$$         val buffer = ByteBuffer.allocateDirect(16 * Int.SIZE_BYTES).asIntBuffer()
    //$$         val params = buffer.also { buffer ->
                    //#if MC >= 1.16.5
                    //$$ GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, buffer)
                    //#else
                    //$$ GL11.glGetInteger(GL11.GL_SCISSOR_BOX, buffer)
                    //#endif
    //$$         }.let { _ ->
    //$$             List(4) { buffer[it] }.toIntArray()
    //$$         }
    //$$
    //$$         return ScissorBox(
    //$$             x = params[0],
    //$$             y = params[1],
    //$$             width = params[2],
    //$$             height = params[3]
    //$$         )
    //$$ }
    //#endif

    @JvmStatic
    public fun applyScissor(box: ScissorBox) {
        //#if MC >= 1.21.5
        activeScissorState = box
        //#else
        //$$ val scaleFactor = OmniResolution.scaleFactor.toFloat()
        //$$ val nx = (box.left * scaleFactor).toInt()
        //$$ val ny = OmniResolution.viewportHeight - box.bottom * scaleFactor.toInt()
        //$$ val nw = (box.width * scaleFactor).toInt()
        //$$ val nh = (box.height * scaleFactor).toInt()
        //#if MC >= 1.17.1
        //$$ GlStateManager._enableScissorTest()
        //$$ GlStateManager._scissorBox(nx, ny, nw, nh)
        //#else
        //$$ GL11.glEnable(GL11.GL_SCISSOR_TEST)
        //$$ GL11.glScissor(nx, ny, nw, nh)
        //#endif
        //#endif
    }

    @JvmStatic
    public fun disableScissor() {
        //#if MC >= 1.21.5
        activeScissorState = null
        //#elseif MC >= 1.17.1
        //$$ GlStateManager._disableScissorTest()
        //#else
        //$$ GL11.glDisable(GL11.GL_SCISSOR_TEST)
        //#endif
    }
}

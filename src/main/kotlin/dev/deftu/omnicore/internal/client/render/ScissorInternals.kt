package dev.deftu.omnicore.internal.client.render

import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.21.6
import net.minecraft.client.gui.DrawContext
//#elseif MC >= 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#elseif MC >= 1.17.1
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#else
//$$ import org.lwjgl.opengl.GL11
//#endif

@ApiStatus.Internal
public object ScissorInternals {
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
        //$$ GL11.glScissor(box.x, box.y, box.width, box.height
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

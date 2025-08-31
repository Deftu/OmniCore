package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import net.minecraft.client.gui.DrawContext

public data class OmniRenderingContext(
    //#if MC >= 1.20.1
    val graphics: DrawContext,
    //#endif
    val matrixStack: OmniMatrixStack,
)

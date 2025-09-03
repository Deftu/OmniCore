package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.VersionedBelow
import dev.deftu.omnicore.client.OmniScreen
import dev.deftu.omnicore.client.events.ScreenEvent
import dev.deftu.omnicore.client.render.vertex.OmniBufferBuilder

//#if MC >= 1.20.1
import net.minecraft.client.gui.DrawContext
//#endif

//#if MC >= 1.16.5 && MC < 1.20.1
//$$ import net.minecraft.client.util.math.MatrixStack
//#endif

/**
 * Provides you with a means of bypassing Minecraft's new render layering system as of Minecraft 1.21.6.
 * Done by overriding the current render target's output color and depth buffers with our own texture(s), drawing your content,
 * to it/them, restoring the original buffers, and then submitting those overriden texture(s) to the renderer to be drawn.
 *
 * It should be noted that this is a very low-level renderer, and as such, it is not recommended to use it unless you know what you are doing,
 * and that it is automatically used to wrap the rendering of [OmniScreen]s, allowing you to use the [OmniBufferBuilder] API to render whatever
 * you please. Use it as you wish elsewhere should you need to.
 */
public object ImmediateScreenRenderer {
    public fun initialize() {
        // no-op below 1.21.6
    }

    @JvmStatic
    public fun render(
        //#if MC >= 1.20.1
        ctx: DrawContext,
        //#elseif MC >= 1.16.5
        //$$ ctx: MatrixStack,
        //#endif
        block: (OmniMatrixStack) -> Unit
    ) {
        val stack = OmniMatrixStack.vanilla(
            //#if MC >= 1.16.5
            ctx
            //#endif
        )

        block(stack)
    }

    @JvmStatic
    @VersionedBelow("1.21.5")
    public fun render(stack: OmniMatrixStack, block: (OmniMatrixStack) -> Unit) {
        block(stack)
    }

    @JvmStatic
    public fun render(event: ScreenEvent.Render, block: (OmniMatrixStack) -> Unit) {
        render(
            //#if MC >= 1.20.1
            event.context,
            //#elseif MC >= 1.16.5
            //$$ event.matrixStack,
            //#endif
            block
        )
    }

    @JvmStatic
    public fun render(painful: Any, block: (OmniMatrixStack) -> Unit) {
        when (painful) {
            //#if MC >= 1.20.1
            is DrawContext -> render(painful, block)
            //#elseif MC >= 1.16.5
            //$$ is MatrixStack -> render(painful, block)
            //#endif
            is OmniMatrixStack -> render(painful, block)
            is ScreenEvent.Render -> render(painful, block)
            else -> throw IllegalArgumentException("Unsupported type for rendering: ${painful::class.java.name}")
        }
    }
}

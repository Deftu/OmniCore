package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import dev.deftu.omnicore.internal.client.render.ScissorInternals

//#if MC >= 1.20.1
import net.minecraft.client.gui.DrawContext
//#endif

//#if MC >= 1.16.5
//$$ import net.minecraft.client.util.math.MatrixStack
//#endif

public data class OmniRenderingContext(
    //#if MC >= 1.20.1
    val graphics: DrawContext,
    //#endif
    val matrices: OmniMatrixStack,
) : AutoCloseable {
    public companion object {
        @JvmStatic
        public fun from(
            //#if MC >= 1.20.1
            ctx: DrawContext
            //#elseif MC >= 1.16.5
            //$$ matrices: MatrixStack
            //#endif
        ): OmniRenderingContext {
            val stack = OmniMatrixStacks.vanilla(
                //#if MC >= 1.16.5
                ctx
                //#endif
            )

            return OmniRenderingContext(
                //#if MC >= 1.20.1
                ctx,
                //#endif
                stack
            )
        }
    }

    public data class ScissorBox(val x: Int, val y: Int, val width: Int, val height: Int) {
        public val isEmpty: Boolean
            get() = width <= 0 || height <= 0

        public fun intersection(other: ScissorBox): ScissorBox? {
            val ix = maxOf(this.x, other.x)
            val iy = maxOf(this.y, other.y)
            val ix2 = minOf(this.x + this.width, other.x + other.width)
            val iy2 = minOf(this.y + this.height, other.y + other.height)
            val iw = ix2 - ix
            val ih = iy2 - iy
            return if (iw > 0 && ih > 0) ScissorBox(ix, iy, iw, ih) else null
        }
    }

    private val scissorStack = ArrayDeque<ScissorBox>()

    /** Pushes a scissor box, intersecting with the current top-level scissor box. */
    public fun pushScissor(x: Int, y: Int, width: Int, height: Int) {
        val newBox = ScissorBox(x, y, width, height)
        if (newBox.isEmpty) {
            scissorStack.addLast(newBox)
            ScissorInternals.applyScissor(
                //#if MC >= 1.21.6
                graphics,
                //#endif
                newBox
            )

            return
        }

        val effectiveBox = scissorStack.lastOrNull()?.intersection(newBox) ?: newBox
        scissorStack.addLast(effectiveBox)
        ScissorInternals.applyScissor(
            //#if MC >= 1.21.6
            graphics,
            //#endif
            effectiveBox
        )
    }

    /** Pops the current scissor; restores previous or disables when empty. */
    public fun popScissor() {
        if (scissorStack.isEmpty()) return
        scissorStack.removeLast()

        val next = scissorStack.lastOrNull()
        if (next == null) {
            ScissorInternals.disableScissor(
                //#if MC >= 1.21.6
                graphics
                //#endif
            )
        } else {
            ScissorInternals.applyScissor(
                //#if MC >= 1.21.6
                graphics,
                //#endif
                next
            )
        }
    }

    public fun doesScissorContain(x: Int, y: Int): Boolean {
        val current = scissorStack.lastOrNull() ?: return true
        return x >= current.x && x < current.x + current.width &&
               y >= current.y && y < current.y + current.height
    }

    public fun withScissor(x: Int, y: Int, width: Int, height: Int, runnable: Runnable) {
        pushScissor(x, y, width, height)

        try {
            runnable.run()
        } finally {
            popScissor()
        }
    }

    /** Submits any necessary closing rendering operations. */
    override fun close() {
    }
}

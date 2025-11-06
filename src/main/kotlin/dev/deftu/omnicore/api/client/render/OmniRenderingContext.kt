package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.textureManager
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStacks
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.internal.client.render.ScissorInternals
import dev.deftu.textile.Text
import net.minecraft.network.chat.Component as VanillaText
import net.minecraft.resources.ResourceLocation
import java.util.function.Consumer

//#if MC >= 1.21.5
import com.mojang.blaze3d.opengl.GlTexture
import dev.deftu.omnicore.internal.client.textures.TextureInternals
//#endif

//#if MC >= 1.20.1
import net.minecraft.client.gui.GuiGraphics
//#endif

//#if MC >= 1.16.5
import com.mojang.blaze3d.vertex.PoseStack
//#endif

public class OmniRenderingContext private constructor(
    //#if MC >= 1.20.1
    @get:JvmName("graphics") public val graphics: GuiGraphics?,
    //#endif
    @get:JvmName("pose") public val pose: OmniPoseStack,
) : AutoCloseable {
    public companion object {
        /**
         * Creates a new [OmniRenderingContext] with a fresh [OmniPoseStack].
         * 
         * This [OmniRenderingContext] does not have an associated [GuiGraphics]. Should you need one, create it using an existing [GuiGraphics] via [from].
         */
        @JvmStatic
        public fun create(): OmniRenderingContext {
            val pose = OmniPoseStacks.create()
            return OmniRenderingContext(
                //#if MC >= 1.20.1
                null,
                //#endif
                pose
            )
        }

        @JvmStatic
        public fun from(
            //#if MC >= 1.20.1
            ctx: GuiGraphics
            //#elseif MC >= 1.16.5
            //$$ pose: PoseStack
            //#endif
        ): OmniRenderingContext {
            val pose = OmniPoseStacks.vanilla(
                //#if MC >= 1.20.1
                ctx
                //#elseif MC >= 1.16.5
                //$$ pose
                //#endif
            )

            return OmniRenderingContext(
                //#if MC >= 1.20.1
                ctx,
                //#endif
                pose
            )
        }

        //#if MC >= 1.20.1
        @JvmStatic
        public fun from(pose: PoseStack): OmniRenderingContext {
            val pose = OmniPoseStacks.vanilla(pose)
            return OmniRenderingContext(null, pose)
        }
        //#endif
    }

    private val scissorStack = ArrayDeque<ScissorBox>()

    public val currentScissor: ScissorBox?
        get() = scissorStack.lastOrNull()

    @JvmOverloads
    public fun renderText(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderText(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderText(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadow)
    }

    public fun renderText(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadowType)
    }

    public fun renderText(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadowType)
    }

    public fun renderText(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadowType)
    }

    @JvmOverloads
    public fun renderTextCentered(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderTextCentered(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderTextCentered(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadow)
    }

    public fun renderTextCentered(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadowType)
    }

    public fun renderTextCentered(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadowType)
    }

    public fun renderTextCentered(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadowType)
    }

    public fun renderGradientQuad(
        pipeline: OmniRenderPipeline,
        x: Float, y: Float,
        width: Int, height: Int,
        topColor: OmniColor,
        bottomColor: OmniColor,
    ) {
        val buffer = pipeline.createBufferBuilder()
        buffer
            .vertex(pose, x.toDouble(), y.toDouble(), 0.0)
            .color(topColor)
            .next()
        buffer
            .vertex(pose, (x + width).toDouble(), y.toDouble(), 0.0)
            .color(topColor)
            .next()
        buffer
            .vertex(pose, (x + width).toDouble(), (y + height).toDouble(), 0.0)
            .color(bottomColor)
            .next()
        buffer
            .vertex(pose, x.toDouble(), (y + height).toDouble(), 0.0)
            .color(bottomColor)
            .next()
        buffer.buildOrThrow().drawAndClose(pipeline)
    }

    public fun renderGradientQuad(
        x: Float, y: Float,
        width: Int, height: Int,
        topColor: OmniColor,
        bottomColor: OmniColor,
    ) {
        renderGradientQuad(OmniRenderPipelines.POSITION_COLOR, x, y, width, height, topColor, bottomColor)
    }

    @JvmOverloads
    public fun renderTextureRegion(
        pipeline: OmniRenderPipeline,
        location: ResourceLocation,
        x: Float, y: Float,
        width: Int, height: Int,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        val buffer = pipeline.createBufferBuilder()
        buffer
            .vertex(pose, x.toDouble(), y.toDouble(), 0.0)
            .texture(u0.toDouble(), v0.toDouble())
            .color(color)
            .next()
        buffer
            .vertex(pose, (x + width).toDouble(), y.toDouble(), 0.0)
            .texture(u1.toDouble(), v0.toDouble())
            .color(color)
            .next()
        buffer
            .vertex(pose, (x + width).toDouble(), (y + height).toDouble(), 0.0)
            .texture(u1.toDouble(), v1.toDouble())
            .color(color)
            .next()
        buffer
            .vertex(pose, x.toDouble(), (y + height).toDouble(), 0.0)
            .texture(u0.toDouble(), v1.toDouble())
            .color(color)
            .next()
        buffer.buildOrThrow().drawAndClose(pipeline) {
            texture(OmniTextureUnit.TEXTURE0, TextureInternals.obtainId(location))
        }
    }

    @JvmOverloads
    public fun renderTextureRegion(
        pipeline: OmniRenderPipeline,
        texture: OmniTextureHandle,
        x: Float, y: Float,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        renderTextureRegion(pipeline, texture.location, x, y, texture.width, texture.height, u0, v0, u1, v1, color)
    }

    @JvmOverloads
    public fun renderTexture(
        pipeline: OmniRenderPipeline,
        location: ResourceLocation,
        x: Float, y: Float,
        width: Int, height: Int,
        u: Float, v: Float,
        textureWidth: Int, textureHeight: Int,
        color: OmniColor = OmniColors.WHITE,
    ) {
        val u1 = u + (width.toFloat() / textureWidth.toFloat())
        val v1 = v + (height.toFloat() / textureHeight.toFloat())
        renderTextureRegion(
            pipeline,
            location,
            x, y,
            width, height,
            u, v,
            u1, v1,
            color
        )
    }

    @JvmOverloads
    public fun renderTexture(
        pipeline: OmniRenderPipeline,
        texture: OmniTextureHandle,
        x: Float, y: Float,
        width: Int, height: Int,
        u: Float, v: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        renderTexture(
            pipeline,
            texture.location,
            x, y,
            width, height,
            u, v,
            texture.width, texture.height,
            color
        )
    }

    // TODO: items, entities

    /** Pushes a scissor box, intersecting with the current top-level scissor box. */
    public fun pushScissor(x: Int, y: Int, width: Int, height: Int) {
        val incoming = ScissorBox(x, y, width, height).transformQuickly(this.pose)

        val effective = scissorStack.lastOrNull()?.let { top ->
            // We already had scissor; clamp to intersection
            top.intersection(incoming) ?: ScissorBox(incoming.x, incoming.y, 0, 0) // <- collapsed to empty
        } ?: incoming // No previous scissor; use new box as-is

        scissorStack.addLast(effective)

        ScissorInternals.applyScissor(effective)
    }

    /** Pops the current scissor; restores previous or disables when empty. */
    public fun popScissor() {
        if (scissorStack.isEmpty()) {
            return
        }

        scissorStack.removeLast()

        val next = scissorStack.lastOrNull()
        if (next == null) {
            ScissorInternals.disableScissor()
        } else {
            ScissorInternals.applyScissor(next)
        }
    }

    public fun doesScissorContain(x: Int, y: Int): Boolean {
        val current = scissorStack.lastOrNull() ?: return true
        return x >= current.x &&
                y >= current.y &&
                x < current.x + current.width &&
                y < current.y + current.height
    }

    public fun doesScissorContain(x: Int, y: Int, width: Int, height: Int): Boolean {
        val current = scissorStack.lastOrNull() ?: return true
        return x >= current.x &&
                y >= current.y &&
                x + width  <= current.x + current.width &&
                y + height <= current.y + current.height
    }

    public fun doesScissorOverlap(x: Int, y: Int, width: Int, height: Int): Boolean {
        val current = scissorStack.lastOrNull() ?: return true
        val disjoint = x + width <= current.x || x >= current.x + current.width ||
                y + height <= current.y || y >= current.y + current.height
        return !disjoint
    }

    public fun withScissor(x: Int, y: Int, width: Int, height: Int, runnable: Runnable) {
        pushScissor(x, y, width, height)

        try {
            runnable.run()
        } finally {
            popScissor()
        }
    }

    public inline fun <T> withScissor(x: Int, y: Int, width: Int, height: Int, supplier: () -> T): T {
        pushScissor(x, y, width, height)

        return try {
            supplier()
        } finally {
            popScissor()
        }
    }

    public fun withPose(consumer: Consumer<OmniPoseStack>) {
        pose.push()
        try {
            consumer.accept(pose)
        } finally {
            pose.pop()
        }
    }

    public inline fun <T> withPose(supplier: () -> T): T {
        pose.push()
        return try {
            supplier()
        } finally {
            pose.pop()
        }
    }

    public inline fun <T> withPose(supplier: (pose: OmniPoseStack) -> T): T {
        pose.push()
        return try {
            supplier(pose)
        } finally {
            pose.pop()
        }
    }

    //#if MC >= 1.20.1
    @Deprecated("Use graphics() instead", replaceWith = ReplaceWith("graphics()"))
    public fun getGraphics(): GuiGraphics? {
        return graphics
    }
    //#endif

    @Deprecated("Use pose() instead", replaceWith = ReplaceWith("pose()"))
    public fun getPose(): OmniPoseStack {
        return pose
    }

    /** Submits any necessary closing rendering operations. */
    override fun close() {
        if (scissorStack.isNotEmpty()) {
            scissorStack.clear()
            ScissorInternals.disableScissor()
        }
    }
}

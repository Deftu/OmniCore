package dev.deftu.omnicore.api.client.framebuffer

import dev.deftu.omnicore.api.client.render.OmniResolution
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStacks
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.client.textures.AbstractGlTexture
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferHelper
import org.lwjgl.opengl.GL11

//#if MC >= 1.21.6
import com.mojang.blaze3d.textures.GpuTextureView
import dev.deftu.omnicore.internal.client.render.OmniGpuTextureView
//#endif

//#if MC >= 1.21.5
import dev.deftu.omnicore.internal.client.render.OmniGpuTexture
//#endif

public interface OmniFramebuffer : AutoCloseable {
    public val id: Int
    public val width: Int
    public val height: Int
    public val colorTexture: AbstractGlTexture

    //#if MC >= 1.21.6
    /**
     * The cached vanilla texture view for the color texture.
     *
     * By default, all implementations will create one on-the-fly, but the in-house implementations cache this value for efficiency.
     */
    public val vanillaColorTextureView: GpuTextureView
        get() = OmniGpuTextureView.framebuffer(this.vanillaColorTexture)
    //#endif

    //#if MC >= 1.21.5
    /**
     * The cached vanilla texture for the color texture.
     *
     * By default, all implementations will create one on-the-fly, but the in-house implementations cache this value for efficiency.
     */
    public val vanillaColorTexture: OmniGpuTexture
        get() = OmniGpuTexture.framebuffer(this.colorTexture, "Default View for Framebuffer Color Texture")
    //#endif

    // !!! We don't include the depth texture here because there's no guarantee that it exists, or it could be a stencil texture alongside being a depth texture

    public fun resize(width: Int, height: Int)

    public fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)

    // Same as stated above about the depth texture, but for clearing depth

    /**
     * Binds this framebuffer for rendering and returns a callable to unbind it.
     */
    public fun bind(): () -> Unit {
        return FramebufferInternals.bind(this)
    }

    public fun <T> using(target: FramebufferTarget, block: () -> T): T {
        if (this.id == -1) {
            this.resize(this.width, this.height)
        }

        return FramebufferHelper.with(target, this, block)
    }

    public fun <T> using(block: () -> T): T {
        return using(FramebufferTarget.READ_WRITE, block)
    }

    public fun <T> usingToRender(block: (pose: OmniPoseStack, width: Int, height: Int) -> T): T {
        return this.using {
            // Define our GL viewport as only being within our framebuffer's bounds
            GL11.glViewport(0, 0, width, height)

            // Undo Minecraft's scaling factor, and the distortion caused by using its projection matrix in a differently sized viewport
            val pose = OmniPoseStacks.create()
            val scale = 1f / OmniResolution.scaleFactor.toFloat()
            pose.scale(scale * OmniResolution.viewportWidth / width, scale * OmniResolution.viewportHeight / height, 1f)

            // Perform the consumer's operation
            val result = block(pose, width, height)

            // Restore all GL states and reset the viewport
            GL11.glViewport(0, 0, OmniResolution.viewportWidth, OmniResolution.viewportHeight)

            // Return the result
            result
        }
    }

    public fun drawColorTexture(
        pipeline: OmniRenderPipeline,
        stack: OmniPoseStack,
        x: Float, y: Float,
        width: Float, height: Float,
        color: OmniColor
    ) {
        this.drawTexture(
            pipeline,
            this.colorTexture,
            stack,
            x, y,
            width, height,
            color
        )
    }

    public fun drawColorTexture(
        stack: OmniPoseStack,
        x: Float, y: Float,
        width: Float, height: Float,
        color: OmniColor
    ) {
        this.drawTexture(
            OmniRenderPipelines.TEXTURED,
            this.colorTexture,
            stack,
            x, y,
            width, height,
            color
        )
    }

    private fun drawTexture(
        pipeline: OmniRenderPipeline,
        texture: OmniTextureHandle,
        stack: OmniPoseStack,
        x: Float, y: Float,
        width: Float, height: Float,
        color: OmniColor
    ) {
        stack.push()
        stack.scale(1f, 1f, 50f)

        val buffer = OmniBufferBuilders.create(pipeline)
        buffer
            .vertex(stack, x.toDouble(), (y + height).toDouble(), 0.0)
            .texture(0.0, 0.0)
            .color(color)
            .next()
        buffer
            .vertex(stack, (x + width).toDouble(), (y + height).toDouble(), 0.0)
            .texture(1.0, 0.0)
            .color(color)
            .next()
        buffer
            .vertex(stack, (x + width).toDouble(), y.toDouble(), 0.0)
            .texture(1.0, 1.0)
            .color(color)
            .next()
        buffer
            .vertex(stack, x.toDouble(), y.toDouble(), 0.0)
            .texture(0.0, 1.0)
            .color(color)
            .next()
        buffer.buildOrNull()?.drawAndClose(pipeline) {
            texture(OmniTextureUnit.TEXTURE0, texture.id)
        }

        stack.pop()
    }
}

package dev.deftu.omnicore.api.client.render

import com.mojang.blaze3d.ProjectionType
import com.mojang.blaze3d.systems.RenderSystem
import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.events.RenderTickEvent
import dev.deftu.omnicore.api.eventBus
import dev.deftu.omnicore.internal.client.render.TemporaryTextureAllocator
import dev.deftu.omnicore.internal.internalLocationOf
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer
import net.minecraft.client.renderer.texture.AbstractTexture

/**
 * Provides you with a means of bypassing Minecraft's new render layering system as of Minecraft 1.21.6.
 * Done by overriding the current render target's output color and depth buffers with our own texture(s), drawing your content,
 * to it/them, restoring the original buffers, and then submitting those overriden texture(s) to the renderer to be drawn.
 *
 * It should be noted that this is a very low-level renderer, and as such, it is not recommended to use it unless you know what you are doing,
 * and that it is automatically used to wrap the rendering of [dev.deftu.omnicore.api.client.screen.OmniScreen]s, allowing you to use the [dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilder] API to render whatever
 * you please. Use it as you wish elsewhere should you need to.
 */
public object ImmediateScreenRenderer {
    private var isInitialized = false
    private var cachedProjectionMatrix: CachedOrthoProjectionMatrixBuffer? = null
    private var textureAllocator = TemporaryTextureAllocator {
        cachedProjectionMatrix?.close()
        cachedProjectionMatrix = null
    }

    @JvmStatic
    public fun initialize() {
        if (isInitialized) {
            return
        }

        eventBus.on<RenderTickEvent.Pre> {
            textureAllocator.tick()
        }

        isInitialized = true
    }

    @JvmStatic
    public fun render(ctx: OmniRenderingContext, block: () -> Unit) {
        val graphics = ctx.graphics ?: throw IllegalStateException("Cannot use ImmediateScreenRenderer outside of a render context!")

        val scaleFactor = OmniResolution.scaleFactor.toFloat()
        val width = OmniResolution.viewportWidth
        val height = OmniResolution.viewportHeight
        val textureAllocation = textureAllocator.allocate(width, height)
        val projectionMatrix = cachedProjectionMatrix ?: CachedOrthoProjectionMatrixBuffer("Immediately rendered screen", 1_000f, 21_000f, true).also { cachedProjectionMatrix = it }

        val prevProjectionBuffer = RenderSystem.getProjectionMatrixBuffer() ?: throw IllegalStateException("No previous projection matrix buffer found!")
        val prevProjectionType = RenderSystem.getProjectionType()
        RenderSystem.setProjectionMatrix(projectionMatrix.getBuffer(width.toFloat() / scaleFactor, height.toFloat() / scaleFactor), ProjectionType.ORTHOGRAPHIC)

        val prevColorOverride = RenderSystem.outputColorTextureOverride
        val prevDepthOverride = RenderSystem.outputDepthTextureOverride
        RenderSystem.outputColorTextureOverride = textureAllocation.colorTextureView
        RenderSystem.outputDepthTextureOverride = textureAllocation.depthTextureView

        ctx.pose.translate(0f, 0f, -10_000f) // Render on the same layer as everything else on the screen
        block()

        RenderSystem.outputColorTextureOverride = prevColorOverride
        RenderSystem.outputDepthTextureOverride = prevDepthOverride
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        RenderSystem.setProjectionMatrix(prevProjectionBuffer, prevProjectionType)

        val identifier = internalLocationOf("__temporary_screen_render__")
        client.textureManager.register(identifier, object : AbstractTexture() {
            init { textureView = textureAllocation.colorTextureView }

            override fun close() {
                // no-op
            }
        })

        graphics.pose().pushMatrix()
        graphics.pose().scale(1f / scaleFactor, 1f / scaleFactor)
        graphics.blit(
            RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
            identifier,
            // x, y
            0, 0,
            // u, v
            0f, height.toFloat(),
            // width, height
            width, height,
            // uWidth, vHeight
            width, -height,
            // textureWidth, textureHeight
            width, height,
        )

        graphics.pose().popMatrix()
        client.textureManager.release(identifier)
    }

    @JvmStatic
    public fun render(ctx: OmniRenderingContext, runnable: Runnable) {
        render(ctx) { runnable.run() }
    }
}

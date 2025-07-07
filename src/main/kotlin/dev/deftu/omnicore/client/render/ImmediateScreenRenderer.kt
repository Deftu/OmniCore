package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.client.OmniScreen
import dev.deftu.omnicore.client.render.vertex.OmniBufferBuilder

//#if MC >= 1.21.6
//$$ import com.mojang.blaze3d.systems.ProjectionType
//$$ import com.mojang.blaze3d.systems.RenderSystem
//$$ import com.mojang.blaze3d.textures.FilterMode
//$$ import com.mojang.blaze3d.textures.GpuTexture
//$$ import com.mojang.blaze3d.textures.GpuTextureView
//$$ import com.mojang.blaze3d.textures.TextureFormat
//$$ import dev.deftu.omnicore.OmniCore
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//$$ import dev.deftu.omnicore.client.OmniClient
//$$ import net.minecraft.client.gl.RenderPipelines
//$$ import net.minecraft.client.render.ProjectionMatrix2
//$$ import net.minecraft.client.texture.AbstractTexture
//#endif

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
 *
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/046e223f9438e00e1c5293ba671cbc5d01642e59/LICENSE
 */
public class ImmediateScreenRenderer : AutoCloseable {

    //#if MC >= 1.21.6
    //$$ private var cachedProjectMatrix: ProjectionMatrix2? = null
    //$$
    //$$ private val temporaryTextureAllocator = TemporaryTextureAllocator {
    //$$     cachedProjectMatrix?.close()
    //$$     cachedProjectMatrix = null
    //$$ }
    //#endif

    public fun render(
        //#if MC >= 1.20.1
        ctx: DrawContext,
        //#elseif MC >= 1.16.5
        //$$ ctx: MatrixStack,
        //#endif
        block: (OmniMatrixStack) -> Unit
    ) {
        //#if MC >= 1.21.6
        //$$ val scaleFactor = OmniResolution.scaleFactor.toFloat()
        //$$ val width = OmniResolution.viewportWidth
        //$$ val height = OmniResolution.viewportHeight
        //$$ val textureAllocation = temporaryTextureAllocator.allocate(width, height)
        //$$
        //$$ val projectionMatrix = cachedProjectMatrix ?: ProjectionMatrix2("Immediately drawn screen", 1_000f, 21_000f, true).apply { cachedProjectMatrix = this }
        //$$ RenderSystem.setProjectionMatrix(projectionMatrix.set(width.toFloat() / scaleFactor, height.toFloat() / scaleFactor), ProjectionType.ORTHOGRAPHIC)
        //$$
        //$$ val previousColorTextureOverride = RenderSystem.outputColorTextureOverride
        //$$ val previousDepthTextureOverride = RenderSystem.outputDepthTextureOverride
        //$$ RenderSystem.outputColorTextureOverride = textureAllocation.colorTextureView
        //$$ RenderSystem.outputDepthTextureOverride = textureAllocation.depthTextureView
        //$$
        //$$ val stack = OmniMatrixStack()
        //$$ stack.translate(0f, 0f, -10_000f) // Ensure we are drawn below everything else
        //$$ block(stack)
        //$$ OmniImage.from(textureAllocation.colorTextureView as net.minecraft.client.texture.GlTextureView).saveTo(kotlin.io.path.Path("ImmediateScreenRenderer_output.png"))
        //$$
        //$$ RenderSystem.outputColorTextureOverride = previousColorTextureOverride
        //$$ RenderSystem.outputDepthTextureOverride = previousDepthTextureOverride
        //$$
        //$$
        //$$ drawTexture(ctx, textureAllocation, scaleFactor)
        //#else
        val stack = OmniMatrixStack.vanilla(
            //#if MC >= 1.16.5
            ctx
            //#endif
        )
        block(stack)
        //#endif
    }

    public fun tick() {
        //#if MC >= 1.21.6
        //$$ temporaryTextureAllocator.renderFrame()
        //#endif
    }

    override fun close() {
        //#if MC >= 1.21.6
        //$$ temporaryTextureAllocator.close()
        //#endif
    }

    //#if MC >= 1.21.6
    //$$ private fun drawTexture(
    //$$     ctx: DrawContext,
    //$$     texture: TextureAllocation,
    //$$     scaleFactor: Float
    //$$ ) {
    //$$     val width = texture.width
    //$$     val height = texture.height
    //$$     val textureManager = OmniTextureManager.get()
    //$$     val identifier = OmniIdentifier.create(OmniCore.ID, "__immediate_render_texture__")
    //$$     textureManager.registerTexture(identifier, ProvidedViewTexture(texture.colorTextureView))
    //$$
    //$$     ctx.matrices.pushMatrix()
    //$$     ctx.matrices.scale(1 / scaleFactor, 1 / scaleFactor) // drawTexture only accepts `int`s
    //$$     ctx.drawTexture(
    //$$         RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
    //$$         identifier,
    //$$         // x, y
    //$$         0, 0,
    //$$         // u, v
    //$$         0f, height.toFloat(),
    //$$         // width, height
    //$$         width, height,
    //$$         // uWidth, vHeight
    //$$         width, -height,
    //$$         // textureWidth, textureHeight
    //$$         width, height,
    //$$     )
    //$$     ctx.matrices.popMatrix()
    //$$     textureManager.destroyTexture(identifier)
    //$$ }
    //$$
    //$$ private class TemporaryTextureAllocator(private val onClose: () -> Unit) : AutoCloseable {
    //$$
    //$$     private val usedAllocations = mutableListOf<TextureAllocation>()
    //$$     private val reusableAllocations = mutableListOf<TextureAllocation>()
    //$$
    //$$     fun allocate(width: Int, height: Int): TextureAllocation {
    //$$         var allocation = reusableAllocations.removeLastOrNull()
    //$$         if (allocation != null && (allocation.width != width || allocation.height != height)) {
    //$$             allocation.close()
    //$$             allocation = null
    //$$         }
    //$$
    //$$         if (allocation == null) {
    //$$             allocation = TextureAllocation(width, height)
    //$$         }
    //$$
    //$$         val device = RenderSystem.getDevice()
    //$$         device.createCommandEncoder().clearColorAndDepthTextures(allocation.colorTexture, 0, allocation.depthTexture, 1.0)
    //$$         usedAllocations.add(allocation)
    //$$         return allocation
    //$$     }
    //$$
    //$$     fun renderFrame() {
    //$$         reusableAllocations.forEach(AutoCloseable::close)
    //$$         reusableAllocations.clear()
    //$$         reusableAllocations.addAll(usedAllocations)
    //$$         usedAllocations.clear()
    //$$         if (reusableAllocations.isEmpty()) {
    //$$             onClose()
    //$$         }
    //$$     }
    //$$
    //$$     override fun close() {
    //$$         // We need to call it twice to ensure all textures are cleaned up
    //$$         renderFrame()
    //$$         renderFrame()
    //$$
    //$$         assert(usedAllocations.isEmpty()) { "Used allocations should be empty after closing" }
    //$$         assert(reusableAllocations.isEmpty()) { "Reusable allocations should be empty after closing" }
    //$$     }
    //$$
    //$$ }
    //$$
    //$$ private data class TextureAllocation(
    //$$     val width: Int,
    //$$     val height: Int,
    //$$ ) : AutoCloseable {
    //$$
    //$$     private val device = RenderSystem.getDevice()
    //$$
    //$$     var colorTexture = device.createTexture(
    //$$         { "Temporary color texture" },
    //$$         GpuTexture.USAGE_RENDER_ATTACHMENT or GpuTexture.USAGE_TEXTURE_BINDING,
    //$$         TextureFormat.RGBA8,
    //$$         width, height,
    //$$         1, 1
    //$$     ).apply { setTextureFilter(FilterMode.NEAREST, false) }
    //$$
    //$$     var depthTexture = device.createTexture(
    //$$         { "Temporary depth texture" },
    //$$         GpuTexture.USAGE_RENDER_ATTACHMENT,
    //$$         TextureFormat.DEPTH32,
    //$$         width, height,
    //$$         1, 1
    //$$     )
    //$$
    //$$     var colorTextureView: GpuTextureView = device.createTextureView(colorTexture)
    //$$
    //$$     var depthTextureView: GpuTextureView = device.createTextureView(depthTexture)
    //$$
    //$$     override fun close() {
    //$$         this.depthTextureView.close()
    //$$         this.colorTextureView.close()
    //$$         this.depthTexture.close()
    //$$         this.colorTexture.close()
    //$$     }
    //$$
    //$$ }
    //$$
    //$$ private class ProvidedViewTexture(private val view: GpuTextureView) : AbstractTexture() {
    //$$
    //$$     init {
    //$$         this.glTextureView = view
    //$$     }
    //$$
    //$$     override fun close() {
    //$$         // no-op
    //$$     }
    //$$
    //$$ }
    //#endif

}

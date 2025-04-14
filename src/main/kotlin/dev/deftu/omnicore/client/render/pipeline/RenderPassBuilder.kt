package dev.deftu.omnicore.client.render.pipeline

import dev.deftu.omnicore.client.render.state.OmniManagedScissorState
import dev.deftu.omnicore.client.render.vertex.OmniBuiltBuffer

//#if MC >= 1.21.5
//$$ import com.mojang.blaze3d.opengl.GlTexture
//$$ import com.mojang.blaze3d.systems.RenderPass
//$$ import com.mojang.blaze3d.systems.RenderSystem
//$$ import com.mojang.blaze3d.textures.TextureFormat
//$$ import dev.deftu.omnicore.client.OmniClient
//$$import java.util.OptionalDouble
//$$ import java.util.OptionalInt
//#endif

public class RenderPassBuilder internal constructor(
    private val renderPass: OmniRenderPass,
    private val pipeline: OmniRenderPipeline,
    private val builtBuffer: OmniBuiltBuffer
) {

    //#if MC >= 1.21.5
    //$$ internal val vanilla: RenderPass
    //#endif

    private var scissorState: OmniManagedScissorState? = null

    init {
        //#if MC >= 1.21.5
        //$$ val builtBuffer = builtBuffer.vanilla
        //$$ val vertexBuffer = pipeline.vertexFormat.uploadImmediateVertexBuffer(builtBuffer.vertexBuffer())
        //$$ val indexBuffer = builtBuffer.indexBuffer()
        //$$ val (uploadedIndexBuffer, indexType) = if (indexBuffer == null) {
        //$$     val shapeIndexBuffer = RenderSystem.getSequentialBuffer(builtBuffer.drawState().mode)
        //$$     shapeIndexBuffer.getBuffer(builtBuffer.drawState().indexCount) to shapeIndexBuffer.type()
        //$$ } else {
        //$$     pipeline.vertexFormat.uploadImmediateIndexBuffer(indexBuffer) to builtBuffer.drawState().indexType
        //$$ }
        //$$
        //$$ vanilla = with(OmniClient.getInstance().mainRenderTarget) {
        //$$     RenderSystem.getDevice()
        //$$         .createCommandEncoder()
        //$$         .createRenderPass(colorTexture!!, OptionalInt.empty(), depthTexture, OptionalDouble.empty())
        //$$ }
        //$$
        //$$ vanilla.setVertexBuffer(0, vertexBuffer)
        //$$ vanilla.setIndexBuffer(uploadedIndexBuffer, indexType)
        //#else
        pipeline.bind()
        //#endif
    }

    public fun texture(name: String, glId: Int): RenderPassBuilder {
        //#if MC >= 1.21.5
        //$$ vanilla.bindSampler(name, DummyTexture(glId))
        //#else
        pipeline.texture(name, glId)
        //#endif
        return this
    }

    public fun texture(index: Int, glId: Int): RenderPassBuilder {
        //#if MC >= 1.21.5
        //$$ val samplerName = pipeline.vanilla.samplers[index]
        //$$ texture(samplerName, glId)
        //#else
        pipeline.texture(index, glId)
        //#endif
        return this
    }

    public fun uniform(name: String, vararg values: Float): RenderPassBuilder {
        //#if MC >= 1.21.5
        //$$ vanilla.setUniform(name, *values)
        //#else
        pipeline.uniform(name, *values)
        //#endif
        return this
    }

    public fun uniform(name: String, vararg values: Int): RenderPassBuilder {
        //#if MC >= 1.21.5
        //$$ vanilla.setUniform(name, *values)
        //#else
        pipeline.uniform(name, *values)
        //#endif
        return this
    }

    public fun enableScissor(x: Int, y: Int, width: Int, height: Int): RenderPassBuilder {
        this.scissorState = OmniManagedScissorState.asEnabled(x, y, width, height)
        return this
    }

    public fun disableScissor(): RenderPassBuilder {
        this.scissorState = OmniManagedScissorState.DISABLED
        return this
    }

    public fun activate() {
        //#if MC >= 1.21.5
        //$$ val scissorState = scissorState ?: OmniManagedScissorState.active()
        //$$ scissorState.applyTo(vanilla)
        //$$ pipeline.draw(vanilla, builtBuffer.vanilla)
        //$$ vanilla.close()
        //#else
        if (renderPass.activePipeline != pipeline) {
            renderPass.activePipeline = pipeline
            pipeline.activeRenderState.apply(renderPass.activeRenderState)
        }

        var previousScissorState: OmniManagedScissorState? = null
        if (this.scissorState != null) {
            previousScissorState = OmniManagedScissorState.active()
            this.scissorState?.activate()
        }

        pipeline.draw(builtBuffer)
        previousScissorState?.activate()
        pipeline.unbind()
        //#endif
    }

    //#if MC >= 1.21.5
    //$$ private class DummyTexture(id: Int) : GlTexture(
    //$$     "",
    //$$     TextureFormat.RGBA8,
    //$$     0, 0, 0,
    //$$     id
    //$$ ) {
    //$$
    //$$     init { modesDirty = false }
    //$$
    //$$ }
    //#endif

}

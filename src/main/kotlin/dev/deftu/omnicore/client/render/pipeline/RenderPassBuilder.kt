package dev.deftu.omnicore.client.render.pipeline

import dev.deftu.omnicore.client.render.state.OmniManagedScissorState
import dev.deftu.omnicore.client.render.vertex.OmniBuiltBuffer

//#if MC >= 1.21.6
import com.mojang.blaze3d.buffers.GpuBuffer
import org.joml.Vector4f
import org.lwjgl.system.MemoryStack
//#endif

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
import com.mojang.blaze3d.systems.RenderPass
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.TextureFormat
import dev.deftu.omnicore.client.render.OmniTextureManager
import dev.deftu.omnicore.client.OmniClient
import java.util.OptionalDouble
import java.util.OptionalInt
//#endif

public class RenderPassBuilder internal constructor(
    private val renderPass: OmniRenderPass,
    private val pipeline: OmniRenderPipeline,
    private val builtBuffer: OmniBuiltBuffer
) {

    //#if MC >= 1.21.6
    private val internalBuffers = mutableSetOf<GpuBuffer>()
    //#endif

    //#if MC >= 1.21.5
    internal val vanilla: RenderPass
    //#endif

    private var scissorState: OmniManagedScissorState? = null

    init {
        //#if MC >= 1.21.6
        val dynamicUniforms = RenderSystem.getDynamicUniforms().write(
            RenderSystem.getModelViewMatrix(),
            Vector4f(1f, 1f, 1f, 1f),
            RenderSystem.getModelOffset(),
            RenderSystem.getTextureMatrix(),
            RenderSystem.getShaderLineWidth()
        )
        //#endif

        //#if MC >= 1.21.5
        val builtBuffer = builtBuffer.vanilla
        val vertexBuffer = pipeline.vertexFormat.uploadImmediateVertexBuffer(builtBuffer.buffer)
        val indexBuffer = builtBuffer.sortedBuffer
        val (uploadedIndexBuffer, indexType) = if (indexBuffer == null) {
            val shapeIndexBuffer = RenderSystem.getSequentialBuffer(builtBuffer.drawParameters.comp_752())
            shapeIndexBuffer.getIndexBuffer(builtBuffer.drawParameters.comp_751()) to shapeIndexBuffer.indexType
        } else {
            pipeline.vertexFormat.uploadImmediateIndexBuffer(indexBuffer) to builtBuffer.drawParameters.comp_753()
        }

        vanilla = with(OmniClient.getInstance().framebuffer) {
            RenderSystem.getDevice()
                .createCommandEncoder()
        //#if MC >= 1.21.6
                .createRenderPass(
                    { "$pipeline render pass" },
                    RenderSystem.outputColorTextureOverride ?: colorAttachmentView!!,
                    OptionalInt.empty(),
                    RenderSystem.outputDepthTextureOverride ?: depthAttachmentView,
                    OptionalDouble.empty()
                )
        //#else
        //$$         .createRenderPass(colorAttachment!!, OptionalInt.empty(), depthAttachment, OptionalDouble.empty())
        //#endif
        }

        vanilla.setVertexBuffer(0, vertexBuffer)
        vanilla.setIndexBuffer(uploadedIndexBuffer, indexType)
        //#if MC >= 1.21.6
        RenderSystem.bindDefaultUniforms(vanilla)
        vanilla.setUniform("DynamicTransforms", dynamicUniforms)
        //#endif
        //#else
        //$$ pipeline.bind()
        //#endif
    }

    public fun texture(name: String, glId: Int): RenderPassBuilder {
        //#if MC >= 1.21.6
        vanilla.bindSampler(name, RenderSystem.getDevice().createTextureView(OmniTextureManager.VanillaWrappedGlTexture(glId)))
        //#elseif MC >= 1.21.5
        //$$ vanilla.bindSampler(name, OmniTextureManager.VanillaWrappedGlTexture(glId))
        //#else
        //$$ pipeline.texture(name, glId)
        //#endif
        return this
    }

    public fun texture(index: Int, glId: Int): RenderPassBuilder {
        //#if MC >= 1.21.5
        val samplerName = pipeline.vanilla.samplers[index]
        texture(samplerName, glId)
        //#else
        //$$ pipeline.texture(index, glId)
        //#endif
        return this
    }

    public fun uniform(name: String, vararg values: Float): RenderPassBuilder {
        //#if MC >= 1.21.6
        vanilla.setUniform(name, MemoryStack.stackPush().use { memoryStack ->
            val buffer = memoryStack.malloc(values.size * 4)
            for (value in values) {
                buffer.putFloat(value)
            }

            buffer.flip()
            RenderSystem.getDevice().createBuffer({ "$name UBO" }, GpuBuffer.USAGE_UNIFORM, buffer)
        }.also(internalBuffers::add))
        //#elseif MC >= 1.21.5
        //$$ vanilla.setUniform(name, *values)
        //#else
        //$$ pipeline.uniform(name, *values)
        //#endif
        return this
    }

    public fun uniform(name: String, vararg values: Int): RenderPassBuilder {
        //#if MC >= 1.21.6
        vanilla.setUniform(name, MemoryStack.stackPush().use { memoryStack ->
            val buffer = memoryStack.malloc(values.size * 4)
            for (value in values) {
                buffer.putInt(value)
            }

            buffer.flip()
            RenderSystem.getDevice().createBuffer({ "$name UBO" }, GpuBuffer.USAGE_UNIFORM, buffer)
        }.also(internalBuffers::add))
        //#elseif MC >= 1.21.5
        //$$ vanilla.setUniform(name, *values)
        //#else
        //$$ pipeline.uniform(name, *values)
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
        val scissorState = scissorState ?: OmniManagedScissorState.active()
        scissorState.applyTo(vanilla)
        pipeline.draw(vanilla, builtBuffer.vanilla)
        vanilla.close()
        //#if MC >= 1.21.6
        internalBuffers.forEach(GpuBuffer::close)
        //#endif
        //#else
        //$$ if (renderPass.activePipeline != pipeline) {
        //$$     renderPass.activePipeline = pipeline
        //$$     pipeline.activeRenderState.apply(renderPass.activeRenderState)
        //$$ }
        //$$
        //$$ var previousScissorState: OmniManagedScissorState? = null
        //$$ if (this.scissorState != null) {
        //$$     previousScissorState = OmniManagedScissorState.active()
        //$$     this.scissorState?.activate()
        //$$ }
        //$$
        //$$ pipeline.draw(builtBuffer)
        //$$ previousScissorState?.activate()
        //$$ pipeline.unbind()
        //#endif
    }

}

package dev.deftu.omnicore.internal.client.render.pipeline

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.RenderPassEncoder
import dev.deftu.omnicore.api.client.render.vertex.OmniBuiltBuffer
import dev.deftu.omnicore.internal.client.textures.WrappedGlTexture
import org.jetbrains.annotations.ApiStatus
import kotlin.use

//#if MC >= 1.21.6
import com.mojang.blaze3d.buffers.GpuBuffer
import org.joml.Vector4f
import org.lwjgl.system.MemoryStack
//#endif

//#if MC >= 1.21.5
import com.mojang.blaze3d.systems.RenderPass
import com.mojang.blaze3d.systems.RenderSystem
import java.util.OptionalDouble
import java.util.OptionalInt
//#endif

@ApiStatus.Internal
public class RenderPassEncoderImpl internal constructor(
    //#if MC < 1.21.5
    //$$ private val renderPass: OmniRenderPass,
    //#endif
    private val renderPipeline: OmniRenderPipeline,
    private val builtBuffer: OmniBuiltBuffer
) : RenderPassEncoder {
    //#if MC >= 1.21.6
    private val internalBuffers = mutableSetOf<GpuBuffer>()
    //#endif

    //#if MC >= 1.21.5
    public val vanilla: RenderPass
    //#endif

    private var scissorBox: OmniRenderingContext.ScissorBox? = null

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
        val vertexBuffer = renderPipeline.vertexFormat.uploadImmediateVertexBuffer(builtBuffer.buffer)
        val indexBuffer = builtBuffer.sortedBuffer
        val (uploadedIndexBuffer, indexType) = if (indexBuffer == null) {
            val shapeIndexBuffer = RenderSystem.getSequentialBuffer(builtBuffer.drawParameters.comp_752())
            shapeIndexBuffer.getIndexBuffer(builtBuffer.drawParameters.comp_751()) to shapeIndexBuffer.indexType
        } else {
            renderPipeline.vertexFormat.uploadImmediateIndexBuffer(indexBuffer) to builtBuffer.drawParameters.comp_753()
        }

        vanilla = with(client.framebuffer) {
            RenderSystem.getDevice()
                .createCommandEncoder()
                //#if MC >= 1.21.6
                .createRenderPass(
                    { "$renderPipeline render pass" },
                    RenderSystem.outputColorTextureOverride ?: colorAttachmentView!!,
                    OptionalInt.empty(),
                    RenderSystem.outputDepthTextureOverride ?: depthAttachmentView,
                    OptionalDouble.empty()
                )
                //#else
                //$$ .createRenderPass(colorAttachment!!, OptionalInt.empty(), depthAttachment, OptionalDouble.empty())
                //#endif
        }

        vanilla.setVertexBuffer(0, vertexBuffer)
        vanilla.setIndexBuffer(uploadedIndexBuffer, indexType)
        //#if MC >= 1.21.6
        RenderSystem.bindDefaultUniforms(vanilla)
        vanilla.setUniform("DynamicTransforms", dynamicUniforms)
        //#endif
        //#else
        //$$ renderPipeline.bind()
        //#endif
    }

    override fun texture(
        name: String,
        id: Int
    ): RenderPassEncoder {
        //#if MC >= 1.21.6
        vanilla.bindSampler(name, RenderSystem.getDevice().createTextureView(WrappedGlTexture(id)))
        //#elseif MC >= 1.21.5
        //$$ vanilla.bindSampler(name, WrappedGlTexture(id))
        //#else
        //$$ pipeline.texture(name, id)
        //#endif
        return this
    }

    override fun texture(
        index: Int,
        id: Int
    ): RenderPassEncoder {
        //#if MC >= 1.21.5
        val samplerName = renderPipeline.vanilla.samplers[index]
        texture(samplerName, id)
        //#else
        //$$ renderPipeline.texture(index, id)
        //#endif
        return this
    }

    override fun uniform(
        name: String,
        vararg values: Float
    ): RenderPassEncoder {
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

    override fun uniform(
        name: String,
        vararg values: Int
    ): RenderPassEncoder {
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

    override fun enableScissor(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): RenderPassEncoder {
        this.scissorBox = OmniRenderingContext.ScissorBox(x, y, width, height)
        return this
    }

    override fun disableScissor(): RenderPassEncoder {
        this.scissorBox = null
        return this
    }

    override fun submit() {
        //#if MC >= 1.21.5
        if (scissorBox != null) {
            vanilla.enableScissor(
                scissorBox!!.x,
                scissorBox!!.y,
                scissorBox!!.width,
                scissorBox!!.height
            )
        } else {
            vanilla.disableScissor()
        }

        (renderPipeline as OmniRenderPipelineImpl).draw(vanilla, builtBuffer.vanilla)
        vanilla.close()
        //#if MC >= 1.21.6
        internalBuffers.forEach(GpuBuffer::close)
        //#endif
        //#else
        //$$ if (renderPass.activePipeline != renderPipeline) {
        //$$     renderPass.activePipeline = renderPipeline
        //$$     renderPipeline.activeRenderState.apply(renderPass.activeRenderState)
        //$$ }
        //$$
        //$$ var previousScissorState: OmniManagedScissorState? = null
        //$$ if (this.scissorState != null) {
        //$$     previousScissorState = OmniManagedScissorState.active()
        //$$     this.scissorState?.activate()
        //$$ }
        //$$
        //$$ (renderPipeline as OmniRenderPipelineImpl).draw(builtBuffer)
        //$$ previousScissorState?.activate()
        //$$ renderPipeline.unbind()
        //#endif
    }
}

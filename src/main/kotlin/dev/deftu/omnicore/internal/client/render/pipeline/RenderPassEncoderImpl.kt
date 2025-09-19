package dev.deftu.omnicore.internal.client.render.pipeline

import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.pipeline.RenderPassEncoder
import dev.deftu.omnicore.api.client.render.vertex.OmniBuiltBuffer
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.21.6
import com.mojang.blaze3d.buffers.GpuBuffer
import org.joml.Vector4f
import org.lwjgl.system.MemoryStack
import kotlin.use
//#endif

//#if MC < 1.21.5
//$$ import dev.deftu.omnicore.internal.client.render.ScissorInternals
//#endif

//#if MC >= 1.21.5
import com.mojang.blaze3d.systems.RenderPass
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.internal.client.textures.WrappedGlTexture
import java.util.OptionalDouble
import java.util.OptionalInt
//#endif

//#if MC >= 1.17.1
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC <= 1.16.5
//$$ import com.mojang.blaze3d.platform.GlStateManager
//$$ import org.lwjgl.opengl.GL11
//#endif

//#if MC <= 1.12.2
//$$ import org.lwjgl.BufferUtils
//#endif

@ApiStatus.Internal
public class RenderPassEncoderImpl internal constructor(
    //#if MC < 1.21.5
    //$$ private val renderPass: OmniRenderPass,
    //#endif
    private val renderPipeline: OmniRenderPipelineImpl,
    private val builtBuffer: OmniBuiltBuffer
) : RenderPassEncoder {
    //#if MC >= 1.21.6
    private val internalBuffers = mutableSetOf<GpuBuffer>()
    private var shaderColor: Vector4f? = null
    private var shaderLineWidth: Float? = null
    //#endif

    //#if MC >= 1.21.5
    override val vanilla: RenderPass
    //#endif

    //#if MC <= 1.21.5
    //$$ private var prevShaderColor: OmniColor? = null
    //#endif

    private var scissorBox: OmniRenderingContext.ScissorBox? = null

    init {
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
        //$$ renderPipeline.texture(name, id)
        //#endif
        return this
    }

    override fun texture(
        unit: OmniTextureUnit,
        id: Int
    ): RenderPassEncoder {
        //#if MC >= 1.21.5
        val samplerName = renderPipeline.vanilla.samplers[unit.id]
        texture(samplerName, id)
        //#else
        //$$ renderPipeline.texture(unit, id)
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
        //$$ renderPipeline.uniform(name, *values)
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
        //$$ renderPipeline.uniform(name, *values)
        //#endif
        return this
    }

    override fun getShaderColor(): OmniColor? {
        //#if MC >= 1.21.6
        return shaderColor?.let {
            OmniColor(it.x, it.y, it.z, it.w)
        }
        //#elseif MC >= 1.17.1
        //$$ val shaderColor = RenderSystem.getShaderColor()
        //$$ return OmniColor(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3])
        //#else
        //#if MC >= 1.16.5
        //$$ val shaderColor = FloatArray(4)
        //$$ GL11.glGetFloatv(GL11.GL_CURRENT_COLOR, shaderColor)
        //#else
        //$$ val shaderColor = BufferUtils.createFloatBuffer(4)
        //$$ GL11.glGetFloat(GL11.GL_CURRENT_COLOR, shaderColor)
        //#endif
        //$$ return OmniColor(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3])
        //#endif
    }

    override fun setShaderColor(red: Float, green: Float, blue: Float, alpha: Float): RenderPassEncoder {
        //#if MC >= 1.21.6
        shaderColor = Vector4f(red, green, blue, alpha)
        //#else
        //$$ prevShaderColor = getShaderColor()
        //#if MC >= 1.17.1
        //$$ RenderSystem.setShaderColor(red, green, blue, alpha)
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.color4f(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.color(red, green, blue, alpha)
        //#endif
        //#endif
        return this
    }

    override fun setLineWidth(width: Float): RenderPassEncoder {
        //#if MC >= 1.21.6
        shaderLineWidth = width
        //#else
        //#if MC >= 1.17.1
        //$$ RenderSystem.lineWidth(width)
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.lineWidth(width)
        //#elseif MC >= 1.12.2
        //$$ GlStateManager.glLineWidth(width)
        //#else
        //$$ GL11.glLineWidth(width)
        //#endif
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
        //#if MC >= 1.21.6
        val dynamicUniforms = RenderSystem.getDynamicUniforms().write(
            RenderSystem.getModelViewMatrix(),
            shaderColor ?: Vector4f(1f, 1f, 1f, 1f),
            //#if MC >= 1.21.9
            //$$ org.joml.Vector3f(),
            //#else
            RenderSystem.getModelOffset(),
            //#endif
            RenderSystem.getTextureMatrix(),
            shaderLineWidth ?: RenderSystem.getShaderLineWidth()
        )
        //#endif

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

        //#if MC >= 1.21.6
        RenderSystem.bindDefaultUniforms(vanilla)
        vanilla.setUniform("DynamicTransforms", dynamicUniforms)
        //#endif
        renderPipeline.draw(vanilla, builtBuffer.vanilla)
        vanilla.close()
        //#if MC >= 1.21.6
        internalBuffers.forEach(GpuBuffer::close)
        //#endif
        //#else
        //$$ if (renderPass.activePipeline != renderPipeline) {
        //$$     renderPass.activePipeline = renderPipeline
        //$$     renderPipeline.activeRenderState.applyTo(renderPass.activeRenderState)
        //$$ }
        //$$
        //$$ var previousScissorBox: OmniRenderingContext.ScissorBox? = null
        //$$ if (this.scissorBox != null) {
        //$$     previousScissorBox = ScissorInternals.activeScissorState
        //$$     this.scissorBox?.let(ScissorInternals::applyScissor)
        //$$ }
        //$$
        //$$ renderPipeline.draw(builtBuffer)
        //$$ previousScissorBox?.let(ScissorInternals::applyScissor) ?: ScissorInternals.disableScissor()
        //$$ renderPipeline.unbind()
        //$$
        //$$ prevShaderColor?.let { setShaderColor(it) }
        //$$ prevShaderColor = null
        //#endif
    }
}

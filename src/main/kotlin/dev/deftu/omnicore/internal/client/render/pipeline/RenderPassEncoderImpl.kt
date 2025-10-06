package dev.deftu.omnicore.internal.client.render.pipeline

import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.math.OmniMatrix4f
import dev.deftu.omnicore.api.client.render.OmniResolution
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.ScissorBox
import dev.deftu.omnicore.api.client.render.pipeline.RenderPassEncoder
import dev.deftu.omnicore.api.client.render.vertex.OmniBuiltBuffer
import dev.deftu.omnicore.internal.client.render.ScissorInternals
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.21.6
import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.GpuBufferSlice
import net.minecraft.client.gl.DynamicUniforms
import org.joml.Vector4f
import org.lwjgl.system.MemoryStack
import kotlin.use
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
import org.joml.Matrix4f
//#endif

//#if MC <= 1.16.5
//$$ import com.mojang.blaze3d.platform.GlStateManager
//$$ import org.lwjgl.BufferUtils
//$$ import org.lwjgl.opengl.GL11
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
    private var dynamicUniforms: DynamicUniforms? = null
    private var dynamicBufferSlice: GpuBufferSlice? = null
    private val internalBuffers = mutableSetOf<GpuBuffer>()

    private var modelViewMatrix: Matrix4f? = null
    private var shaderColor: Vector4f? = null
    private var textureMatrix: Matrix4f? = null
    private var shaderLineWidth: Float? = null
    //#endif

    //#if MC >= 1.21.5
    private val namedSamplers = hashMapOf<String, Int>()
    private val unitSamplers = hashMapOf<OmniTextureUnit, Int>()
    private val uniformsf = hashMapOf<String, FloatArray>()
    private val uniformsi = hashMapOf<String, IntArray>()

    override lateinit var vanilla: RenderPass
        private set
    //#endif

    //#if MC <= 1.21.5
    //$$ private var prevShaderColor: OmniColor? = null
    //#endif

    //#if MC <= 1.16.5
    //$$ private var lineStippleFactor: Int = 1
    //$$ private var lineStipplePattern: Short = 0xFFFF.toShort()
    //#endif

    private var scissorBox: ScissorBox? = null

    init {
        //#if MC >= 1.21.6
        dynamicUniforms = RenderSystem.getDynamicUniforms()
        dynamicBufferSlice = bindDynamicUniforms()
        //#endif

        //#if MC < 1.21.5
        //$$ renderPipeline.bind()
        //#endif
    }

    //#if MC >= 1.21.5
    public fun initialize() {
        if (this::vanilla.isInitialized) {
            return
        }

        val builtBuffer = builtBuffer.vanilla
        val vertexBuffer = renderPipeline.vertexFormat.uploadImmediateVertexBuffer(builtBuffer.buffer)
        val indexBuffer = builtBuffer.sortedBuffer
        val (uploadedIndexBuffer, indexType) = if (indexBuffer == null) {
            val shapeIndexBuffer = RenderSystem.getSequentialBuffer(builtBuffer.drawParameters.mode)
            shapeIndexBuffer.getIndexBuffer(builtBuffer.drawParameters.indexCount) to shapeIndexBuffer.indexType
        } else {
            renderPipeline.vertexFormat.uploadImmediateIndexBuffer(indexBuffer) to builtBuffer.drawParameters.indexType
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

        for ((name, id) in namedSamplers) {
            //#if MC >= 1.21.6
            vanilla.bindSampler(name, RenderSystem.getDevice().createTextureView(WrappedGlTexture(id)))
            //#elseif MC >= 1.21.5
            //$$ vanilla.bindSampler(name, WrappedGlTexture(id))
            //#endif
        }

        for ((unit, id) in unitSamplers) {
            val samplerName = renderPipeline.vanilla.samplers[unit.id]
            //#if MC >= 1.21.6
            vanilla.bindSampler(samplerName, RenderSystem.getDevice().createTextureView(WrappedGlTexture(id)))
            //#elseif MC >= 1.21.5
            //$$ vanilla.bindSampler(samplerName, WrappedGlTexture(id))
            //#endif
        }

        for ((name, values) in uniformsf) {
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
            //#endif
        }

        for ((name, values) in uniformsi) {
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
            //#endif
        }
    }
    //#endif

    override fun texture(
        name: String,
        id: Int
    ): RenderPassEncoder {
        //#if MC >= 1.21.5
        namedSamplers[name] = id
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
        unitSamplers[unit] = id
        //#else
        //$$ renderPipeline.texture(unit, id)
        //#endif
        return this
    }

    override fun uniform(
        name: String,
        vararg values: Float
    ): RenderPassEncoder {
        //#if MC >= 1.21.5
        uniformsf[name] = values
        //#else
        //$$ renderPipeline.uniform(name, *values)
        //#endif
        return this
    }

    override fun uniform(
        name: String,
        vararg values: Int
    ): RenderPassEncoder {
        //#if MC >= 1.21.5
        uniformsi[name] = values
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
        //$$ val shaderColor = BufferUtils.createFloatBuffer(16)
        //$$ GL11.glGetFloat(GL11.GL_CURRENT_COLOR, shaderColor)
        //#endif
        //$$ return OmniColor(shaderColor[0], shaderColor[1], shaderColor[2], shaderColor[3])
        //#endif
    }

    override fun setShaderColor(red: Float, green: Float, blue: Float, alpha: Float): RenderPassEncoder {
        //#if MC >= 1.21.6
        shaderColor = Vector4f(red, green, blue, alpha)
        dynamicBufferSlice = bindDynamicUniforms()
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
        dynamicBufferSlice = bindDynamicUniforms()
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

    override fun setLineStipple(factor: Int, pattern: Short): RenderPassEncoder {
        //#if MC <= 1.16.5
        //$$ lineStippleFactor = factor
        //$$ lineStipplePattern = pattern
        //#endif
        return this
    }

    override fun setTextureMatrix(matrix: OmniMatrix4f): RenderPassEncoder {
        //#if MC >= 1.21.6
        textureMatrix = matrix.vanilla
        dynamicBufferSlice = bindDynamicUniforms()
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.setTextureMatrix(matrix.vanilla)
        //#else
        //$$ GL11.glMatrixMode(GL11.GL_TEXTURE)
        //$$ val buffer = BufferUtils.createFloatBuffer(16).put(matrix.toArray()).flip()
        //#if MC >= 1.16.5
        //$$ GL11.glLoadMatrixf(buffer)
        //#else
        //$$ GL11.glLoadMatrix(buffer)
        //#endif
        //$$ GL11.glMatrixMode(GL11.GL_MODELVIEW)
        //#endif
        return this
    }

    override fun resetTextureMatrix(): RenderPassEncoder {
        //#if MC >= 1.21.6
        textureMatrix = null
        dynamicBufferSlice = bindDynamicUniforms()
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.resetTextureMatrix()
        //#else
        //$$ GL11.glMatrixMode(GL11.GL_TEXTURE)
        //$$ GL11.glLoadIdentity()
        //$$ GL11.glMatrixMode(GL11.GL_MODELVIEW)
        //#endif
        return this
    }

    override fun setModelViewMatrix(matrix: OmniMatrix4f): RenderPassEncoder {
        //#if MC >= 1.21.6
        modelViewMatrix = matrix.vanilla
        dynamicBufferSlice = bindDynamicUniforms()
        //#elseif MC >= 1.17.1
        //$$ val stack = RenderSystem.getModelViewStack()
        //#if MC >= 1.20.6
        //$$ stack.identity()
        //$$ stack.mul(matrix.vanilla)
        //#else
        //$$ stack.loadIdentity()
        //$$ stack.multiplyPositionMatrix(matrix.vanilla)
        //$$ RenderSystem.applyModelViewMatrix()
        //#endif
        //#else
        //$$ GL11.glMatrixMode(GL11.GL_MODELVIEW)
        //$$ val buffer = BufferUtils.createFloatBuffer(16).put(matrix.toArray()).flip()
        //#if MC >= 1.16.5
        //$$ GL11.glLoadMatrixf(buffer)
        //#else
        //$$ GL11.glLoadMatrix(buffer)
        //#endif
        //#endif
        return this
    }

    override fun resetModelViewMatrix(): RenderPassEncoder {
        //#if MC >= 1.21.6
        modelViewMatrix = null
        dynamicBufferSlice = bindDynamicUniforms()
        //#elseif MC >= 1.17.1
        //$$ val stack = RenderSystem.getModelViewStack()
        //#if MC >= 1.20.6
        //$$ stack.identity()
        //#else
        //$$ stack.loadIdentity()
        //$$ RenderSystem.applyModelViewMatrix()
        //#endif
        //#else
        //$$ GL11.glMatrixMode(GL11.GL_MODELVIEW)
        //$$ GL11.glLoadIdentity()
        //#endif
        return this
    }

    override fun enableScissor(box: ScissorBox): RenderPassEncoder {
        this.scissorBox = box
        return this
    }

    override fun disableScissor(): RenderPassEncoder {
        this.scissorBox = null
        return this
    }

    override fun submit() {
        //#if MC >= 1.21.5
        initialize()

        //#if MC >= 1.21.6
        dynamicBufferSlice = bindDynamicUniforms()
        //#endif

        val scissorBox = scissorBox ?: ScissorInternals.activeScissorState
        scissorBox?.let { box ->
            val scaleFactor = OmniResolution.scaleFactor.toFloat()
            val nx = (box.left * scaleFactor).toInt()
            val ny = OmniResolution.viewportHeight - box.bottom * scaleFactor.toInt()
            val nw = (box.width * scaleFactor).toInt()
            val nh = (box.height * scaleFactor).toInt()
            vanilla.enableScissor(nx, ny, nw, nh)
        } ?: vanilla.disableScissor()

        //#if MC >= 1.21.6
        RenderSystem.bindDefaultUniforms(vanilla)
        vanilla.setUniform("DynamicTransforms", dynamicBufferSlice)
        //#endif

        renderPipeline.draw(vanilla, builtBuffer.vanilla)
        vanilla.close()
        //#if MC >= 1.21.6
        internalBuffers.forEach(GpuBuffer::close)
        //#endif
        //#else
        //$$ if (renderPass.activePipeline != renderPipeline) {
        //$$     renderPass.activePipeline = renderPipeline
        //$$     renderPipeline.requestedRenderState.applyTo(renderPass.activeRenderState)
        //$$
            //#if MC <= 1.16.5
            //$$ // Update line stipple
            //$$ renderPass.activeRenderState.legacyState.setLineStippleState(renderPass.activeRenderState.legacyState.lineStippleState.withParams(lineStippleFactor, lineStipplePattern))
            //#endif
        //$$ }
        //$$
        //$$ var previousScissorBox: ScissorBox? = null
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

    //#if MC >= 1.21.6
    private fun bindDynamicUniforms(): GpuBufferSlice? {
        return dynamicUniforms?.write(
            modelViewMatrix ?: RenderSystem.getModelViewMatrix(),
            shaderColor ?: Vector4f(1f, 1f, 1f, 1f),
            //#if MC >= 1.21.9
            //$$ org.joml.Vector3f(),
            //#else
            RenderSystem.getModelOffset(),
            //#endif
            textureMatrix ?: RenderSystem.getTextureMatrix(),
            shaderLineWidth ?: RenderSystem.getShaderLineWidth()
        )
    }
    //#endif
}

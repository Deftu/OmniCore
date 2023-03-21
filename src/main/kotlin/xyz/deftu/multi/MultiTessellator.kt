package xyz.deftu.multi

//#if MC>=11700
import net.minecraft.client.gl.ShaderProgram
//#endif

//#if MC>=11600
import net.minecraft.client.render.RenderLayer
//#endif

//#if MC>=11400
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
//#endif

//#if MC<=11902
//$$ import net.minecraft.util.math.Matrix4f
//$$ import net.minecraft.util.math.Matrix3f
//#endif

//#if MC<=11502
//$$ import net.minecraft.client.util.math.Vector4f
//#endif

import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.function.Supplier
import kotlin.reflect.KFunction
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormatElement

class MultiTessellator(
    private val buffer: BufferBuilder
) {
    companion object {
        @JvmStatic fun getTessellator() = Tessellator.getInstance()
        @JvmStatic fun getFromBuffer() =
            //#if MC>=11200
            MultiTessellator(getTessellator().buffer)
            //#else
            //$$ MultiTessellator(getTessellator().worldRenderer)
            //#endif
        @JvmStatic fun getWithBuffer(buffer: BufferBuilder) = MultiTessellator(buffer)
        @JvmStatic fun getFromSize(size: Int) = getWithBuffer(BufferBuilder(size))

        //#if MC>=11700
        @JvmStatic val defaultShaders = mapOf<VertexFormat, Supplier<ShaderProgram?>>(
            net.minecraft.client.render.VertexFormats.LINES to referenceToSupplier(GameRenderer::getRenderTypeLinesProgram),
            net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_LIGHT to referenceToSupplier(GameRenderer::getParticleProgram),
            net.minecraft.client.render.VertexFormats.POSITION to referenceToSupplier(GameRenderer::getPositionProgram),
            net.minecraft.client.render.VertexFormats.POSITION_COLOR to referenceToSupplier(GameRenderer::getPositionColorProgram),
            net.minecraft.client.render.VertexFormats.POSITION_COLOR_LIGHT to referenceToSupplier(GameRenderer::getPositionColorLightmapProgram),
            net.minecraft.client.render.VertexFormats.POSITION_TEXTURE to referenceToSupplier(GameRenderer::getPositionTexProgram),
            net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE to referenceToSupplier(GameRenderer::getPositionColorTexProgram),
            net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR to referenceToSupplier(GameRenderer::getPositionTexColorProgram),
            net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE_LIGHT to referenceToSupplier(GameRenderer::getPositionColorTexLightmapProgram),
            net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_LIGHT_COLOR to referenceToSupplier(GameRenderer::getPositionTexLightmapColorProgram),
            net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_NORMAL to referenceToSupplier(GameRenderer::getPositionTexColorNormalProgram)
        )

        private fun <T> referenceToSupplier(reference: KFunction<T>): Supplier<T> {
            return Supplier { reference.call() }
        }
        //#endif
    }

    private var currentVertexFormat: VertexFormat? = null
    //#if MC>=11600
    private var renderLayer: RenderLayer? = null
    //#endif

    fun beginWithActiveShader(mode: DrawModes, format: VertexFormat) = apply {
        currentVertexFormat = format
        buffer.begin(mode.vanilla, format)
    }

    fun beginWithActiveShader(mode: DrawModes, format: VertexFormats) = apply {
        beginWithActiveShader(mode, format.vanilla)
    }

    fun beginWithDefaultShader(mode: DrawModes, format: VertexFormat) = apply {
        //#if MC>=11700
        val supplier = defaultShaders[format] ?: error("Unsupported vertex format $format - no default shader")
        MultiRenderSystem.setShader(supplier)
        //#endif
        beginWithActiveShader(mode, format)
    }

    fun beginWithDefaultShader(mode: DrawModes, format: VertexFormats) = apply {
        beginWithDefaultShader(mode, format.vanilla)
    }

    //#if MC>=11600
    fun beginRenderLayer(layer: RenderLayer) = apply {
        renderLayer = layer
        beginWithActiveShader(DrawModes.fromRenderLayer(layer), layer.vertexFormat)
    }
    //#endif

    fun draw() {
        //#if MC>=11600
        if (renderLayer != null) {
            renderLayer!!.draw(buffer, 0, 0, 0)
            return
        }

        //#endif
        handleDraw()
    }

    private fun handleDraw() {
        if (currentVertexFormat == null) drawBuffer()

        //#if MC<11700
        //$$ val wantEnabledStates = getDesiredTextureUnitState(currentVertexFormat!!)
        //$$ val wasEnabledStates = BooleanArray(wantEnabledStates.size)
        //$$ for (i in wasEnabledStates.indices) {
        //$$     MultiTextureManager.configureTextureUnit(i) {
        //$$         val isEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D).also { wasEnabledStates[i] = it }
        //$$         val wantEnabled = wantEnabledStates[i]
        //$$         if (isEnabled != wantEnabled) {
        //$$             if (wantEnabled) {
        //$$                 MultiGlStateManager.enableBasicTexture2D()
        //$$             } else {
        //$$                 MultiGlStateManager.disableBasicTexture2D()
        //$$             }
        //$$         }
        //$$     }
        //$$ }
        //#endif

        drawBuffer()

        //#if MC<11700
        //$$ for (i in wasEnabledStates.indices) {
        //$$     if (wasEnabledStates[i] == wantEnabledStates[i]) {
        //$$         continue
        //$$     }
        //$$
        //$$     if (wasEnabledStates[i]) {
        //$$         MultiTextureManager.configureTextureUnit(i, MultiGlStateManager::enableBasicTexture2D)
        //$$     } else {
        //$$         MultiTextureManager.configureTextureUnit(i, MultiGlStateManager::disableBasicTexture2D)
        //$$     }
        //$$ }
        //#endif
    }

    private fun drawBuffer() {
        //#if MC>=11600
        if (buffer == getTessellator().buffer) {
            getTessellator().draw()
        } else {
            //#if MC>=11900
            BufferRenderer.drawWithGlobalProgram(buffer.end())
            //#else
            //$$ BufferRenderer.draw(buffer)
            //#endif
        }
        //#else
        //$$ getTessellator().draw()
        //#endif
    }

    fun vertex(
        stack: MultiMatrixStack,
        x: Float,
        y: Float,
        z: Float
    ) = apply {
        //#if MC>=11600
        buffer.vertex(stack.peek().matrix, x, y, z)
        //#else
        //$$ val vec = Vector4f(x, y, z, 1f)
        //#if MC>=11400
        //$$ vec.transform(stack.peek().matrix)
        //#else
        //$$ Matrix4f.transform(stack.peek().matrix, vec, vec)
        //#endif
        //#if MC>=11400
        //$$ buffer.vertex(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
        //#else
        //$$ buffer.pos(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
        //#endif
        //#endif
    }

    fun normal(
        stack: MultiMatrixStack,
        x: Float,
        y: Float,
        z: Float
    ) = apply {
        //#if MC>=11500
        buffer.normal(stack.peek().normal, x, y, z)
        //#else
        //$$ val vec = org.lwjgl.util.vector.Vector3f(x, y, z)
        //#if MC>=11400
        //$$ vec.transform(stack.peek().normal)
        //#else
        //$$ Matrix3f.transform(stack.peek().normal, vec, vec)
        //#endif
        //$$ buffer.normal(vec.x, vec.y, vec.z)
        //#endif
    }

    fun color(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) = apply {
        buffer.color(red, green, blue, alpha)
    }

    fun color(
        red: Int,
        green: Int,
        blue: Int,
        alpha: Int
    ) = color(
        red / 255.0f,
        green / 255.0f,
        blue / 255.0f,
        alpha / 255.0f
    )

    fun color(
        color: Int
    ) = color(
        (color shr 16 and 255) / 255.0f,
        (color shr 8 and 255) / 255.0f,
        (color and 255) / 255.0f,
        (color shr 24 and 255) / 255.0f
    )

    fun color(
        color: Color
    ) = color(
        color.red / 255.0f,
        color.green / 255.0f,
        color.blue / 255.0f,
        color.alpha / 255.0f
    )

    fun texture(
        u: Float,
        v: Float
    ) = apply {
        //#if MC>=11500
        buffer.texture(u, v)
        //#else
        //#if MC>=11400
        //$$ buffer.texture(u.toDouble(), v.toDouble())
        //#else
        //$$ buffer.tex(u.toDouble(), v.toDouble())
        //#endif
        //#endif
    }

    fun overlay(
        u: Int,
        v: Int
    ) = apply {
        //#if MC>=11500
        buffer.overlay(u, v)
        //#else
        //#if MC>=11400
        //$$ buffer.texture(u.toShort(), v.toShort())
        //#else
        //$$ buffer.tex(u.toDouble(), v.toDouble())
        //#endif
        //#endif
    }

    fun light(
        u: Int,
        v: Int
    ) = apply {
        //#if MC>=11400
        buffer.light(u, v)
        //#else
        //$$ buffer.lightmap(u, v)
        //#endif
    }

    fun next() = apply {
        //#if MC>=11400
        buffer.next()
        //#else
        //$$ buffer.endVertex()
        //#endif
    }

    private fun getDesiredTextureUnitState(vertexFormat: VertexFormat): BooleanArray {
        var wantEnabled = BooleanArray(2)
        for (element in vertexFormat.elements) {
            //#if MC>=11400
            if (element.type === VertexFormatElement.Type.UV) {
                val index: Int = element.uvIndex
            //#else
            //$$ if (element.usage == VertexFormatElement.EnumUsage.UV) {
            //$$     val index: Int = element.index
            //#endif
                if (wantEnabled.size <= index)
                    wantEnabled = wantEnabled.copyOf(index + 1)
                wantEnabled[index] = true
            }
        }

        return wantEnabled
    }

    enum class VertexFormats(
        val vanilla: VertexFormat
    ) {
        POSITION(net.minecraft.client.render.VertexFormats.POSITION),
        POSITION_COLOR(net.minecraft.client.render.VertexFormats.POSITION_COLOR),
        POSITION_TEXTURE(net.minecraft.client.render.VertexFormats.POSITION_TEXTURE),
        POSITION_TEXTURE_COLOR(net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR)
    }

    enum class DrawModes(
        val gl: Int
    ) {
        LINES(GL11.GL_LINES),
        LINE_STRIP(GL11.GL_LINE_STRIP),
        TRIANGLES(GL11.GL_TRIANGLES),
        TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
        TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
        QUADS(GL11.GL_QUADS);

        //#if MC>=11700
        val vanilla: VertexFormat.DrawMode
        //#else
        //$$ val vanilla: Int
        //#endif

        init {
            //#if MC>=11700
            vanilla = when (gl) {
                GL11.GL_LINES -> VertexFormat.DrawMode.LINES
                GL11.GL_LINE_STRIP -> VertexFormat.DrawMode.LINE_STRIP
                GL11.GL_TRIANGLES -> VertexFormat.DrawMode.TRIANGLES
                GL11.GL_TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP
                GL11.GL_TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN
                GL11.GL_QUADS -> VertexFormat.DrawMode.QUADS
                else -> throw IllegalArgumentException("Unsupported draw mode $gl")
            }
            //#else
            //$$ vanilla = gl
            //#endif
        }

        companion object {
            @JvmStatic fun fromGl(gl: Int): DrawModes {
                return when (gl) {
                    GL11.GL_LINES -> LINES
                    GL11.GL_LINE_STRIP -> LINE_STRIP
                    GL11.GL_TRIANGLES -> TRIANGLES
                    GL11.GL_TRIANGLE_STRIP -> TRIANGLE_STRIP
                    GL11.GL_TRIANGLE_FAN -> TRIANGLE_FAN
                    GL11.GL_QUADS -> QUADS
                    else -> throw IllegalArgumentException("Unsupported draw mode $gl")
                }
            }

            //#if MC>=11700
            @JvmStatic fun glToVanillaDrawMode(glMode: Int): VertexFormat.DrawMode {
                return when (glMode) {
                    GL11.GL_LINES -> VertexFormat.DrawMode.LINES
                    GL11.GL_LINE_STRIP -> VertexFormat.DrawMode.LINE_STRIP
                    GL11.GL_TRIANGLES -> VertexFormat.DrawMode.TRIANGLES
                    GL11.GL_TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP
                    GL11.GL_TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN
                    GL11.GL_QUADS -> VertexFormat.DrawMode.QUADS
                    else -> throw IllegalArgumentException("Unsupported draw mode $glMode")
                }
            }

            @JvmStatic fun fromMc(mcMode: VertexFormat.DrawMode): DrawModes {
                return when (mcMode) {
                    VertexFormat.DrawMode.LINES -> LINES
                    VertexFormat.DrawMode.LINE_STRIP -> LINE_STRIP
                    VertexFormat.DrawMode.TRIANGLES -> TRIANGLES
                    VertexFormat.DrawMode.TRIANGLE_STRIP -> TRIANGLE_STRIP
                    VertexFormat.DrawMode.TRIANGLE_FAN -> TRIANGLE_FAN
                    VertexFormat.DrawMode.QUADS -> QUADS
                    else -> throw IllegalArgumentException("Unsupported draw mode $mcMode")
                }
            }
            //#endif

            //#if MC>=11600
            @JvmStatic fun fromRenderLayer(layer: RenderLayer): DrawModes {
                //#if MC>=11700
                return fromMc(layer.drawMode)
                //#else
                //$$ return fromGl(layer.drawMode)
                //#endif
            }
            //#endif
        }
    }
}

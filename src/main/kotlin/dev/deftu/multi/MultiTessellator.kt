package dev.deftu.multi

//#if MC >= 1.20
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC >= 1.17
import net.minecraft.client.gl.ShaderProgram
//#endif

//#if MC >= 1.16
import net.minecraft.client.render.RenderLayer
//#endif

//#if MC >= 1.14
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
//#endif

//#if MC <= 1.19.2
//$$ import net.minecraft.util.math.Matrix4f
//$$ import net.minecraft.util.math.Matrix3f
//#endif

//#if MC <= 1.12.2
//$$ import org.lwjgl.util.vector.Vector4f
//#endif

import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.function.Supplier
import kotlin.reflect.KFunction
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormatElement
import java.util.IdentityHashMap

class MultiTessellator(
    private val buffer: BufferBuilder
) {
    companion object {
        @JvmStatic fun getTessellator() = Tessellator.getInstance()
        @JvmStatic fun getFromBuffer() =
            //#if MC >= 1.12
            MultiTessellator(getTessellator().buffer)
            //#else
            //$$ MultiTessellator(getTessellator().worldRenderer)
            //#endif
        @JvmStatic fun getWithBuffer(buffer: BufferBuilder) = MultiTessellator(buffer)
        @JvmStatic fun getFromSize(size: Int) = getWithBuffer(BufferBuilder(size))

        //#if MC >= 1.17
        @JvmStatic val defaultShaders by lazy {
            val value = IdentityHashMap<VertexFormat, Supplier<ShaderProgram?>>()

            value[net.minecraft.client.render.VertexFormats.POSITION] = Supplier { GameRenderer.getPositionProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR] = Supplier { GameRenderer.getPositionColorProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE] = Supplier { GameRenderer.getPositionTexProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR] = Supplier { GameRenderer.getPositionTexColorProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_LIGHT] = Supplier { GameRenderer.getParticleProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE_LIGHT] = Supplier { GameRenderer.getPositionColorTexLightmapProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_LIGHT_COLOR] = Supplier { GameRenderer.getPositionTexLightmapColorProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_NORMAL] = Supplier { GameRenderer.getPositionTexColorNormalProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE] = Supplier { GameRenderer.getPositionColorTexProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_LIGHT] = Supplier { GameRenderer.getPositionColorLightmapProgram() }
            value[net.minecraft.client.render.VertexFormats.LINES] = Supplier { GameRenderer.getRenderTypeLinesProgram() }
            value[net.minecraft.client.render.VertexFormats.BLIT_SCREEN] = Supplier { MultiClient.getInstance().gameRenderer.blitScreenProgram }

            value
        }

        private fun <T> referenceToSupplier(reference: KFunction<T>): Supplier<T> {
            return Supplier { reference.call() }
        }
        //#endif
    }

    private var currentVertexFormat: VertexFormat? = null
    //#if MC >= 1.16
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
        //#if MC >= 1.17
        val supplier = defaultShaders[format] ?: error("Unsupported vertex format '$format' - no default shader")
        MultiRenderSystem.setShader(supplier)
        //#endif
        beginWithActiveShader(mode, format)
    }

    fun beginWithDefaultShader(mode: DrawModes, format: VertexFormats) = apply {
        beginWithDefaultShader(mode, format.vanilla)
    }

    //#if MC >= 1.16
    fun beginRenderLayer(layer: RenderLayer) = apply {
        renderLayer = layer
        beginWithActiveShader(DrawModes.fromRenderLayer(layer), layer.vertexFormat)
    }
    //#endif

    fun draw() {
        //#if MC >= 1.16
        if (renderLayer != null) {
            //#if MC >= 1.20
            renderLayer!!.draw(buffer, RenderSystem.getVertexSorting())
            //#else
            //$$ renderLayer!!.draw(buffer, 0, 0, 0)
            //#endif
            return
        }

        //#endif
        handleDraw()
    }

    private fun handleDraw() {
        if (currentVertexFormat == null) drawBuffer()

        //#if MC < 1.17
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

        //#if MC < 1.17
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
        //#if MC >= 1.16
        if (buffer == getTessellator().buffer) {
            getTessellator().draw()
        } else {
            //#if MC >= 1.19
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
        //#if MC >= 1.16
        buffer.vertex(stack.peek().matrix, x, y, z)
        //#else
        //$$ val vec = Vector4f(x, y, z, 1f)
        //#if MC >= 1.14
        //$$ vec.transform(stack.peek().matrix)
        //#else
        //$$ Matrix4f.transform(stack.peek().matrix, vec, vec)
        //#endif
        //#if MC >= 1.14
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
        //#if MC >= 1.15
        buffer.normal(stack.peek().normal, x, y, z)
        //#else
        //$$ val vec = org.lwjgl.util.vector.Vector3f(x, y, z)
        //#if MC >= 1.14
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
        //#if MC >= 1.15
        buffer.texture(u, v)
        //#else
        //#if MC >= 1.14
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
        //#if MC >= 1.15
        buffer.overlay(u, v)
        //#else
        //#if MC >= 1.14
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
        //#if MC >= 1.14
        buffer.light(u, v)
        //#else
        //$$ buffer.lightmap(u, v)
        //#endif
    }

    fun next() = apply {
        //#if MC >= 1.14
        buffer.next()
        //#else
        //$$ buffer.endVertex()
        //#endif
    }

    private fun getDesiredTextureUnitState(vertexFormat: VertexFormat): BooleanArray {
        var wantEnabled = BooleanArray(2)
        for (element in vertexFormat.elements) {
            //#if MC >= 1.14
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

        //#if MC >= 1.17
        val vanilla: VertexFormat.DrawMode
        //#else
        //$$ val vanilla: Int
        //#endif

        init {
            //#if MC >= 1.17
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

            //#if MC >= 1.17
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

            //#if MC >= 1.16
            @JvmStatic fun fromRenderLayer(layer: RenderLayer): DrawModes {
                //#if MC >= 1.17
                return fromMc(layer.drawMode)
                //#else
                //$$ return fromGl(layer.drawMode)
                //#endif
            }
            //#endif
        }
    }
}

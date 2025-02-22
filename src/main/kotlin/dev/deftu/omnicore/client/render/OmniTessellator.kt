@file:Suppress("MemberVisibilityCanBePrivate", "JoinDeclarationAndAssignment", "unused", "CanBeParameter")

package dev.deftu.omnicore.client.render

//#if MC >= 1.21.4
//$$ import net.minecraft.client.gl.ShaderProgramKeys
//$$ import net.minecraft.client.gl.ShaderProgramKey
//#endif

//#if MC >= 1.21.1
//$$ import com.mojang.blaze3d.vertex.MeshData
//$$ import com.mojang.blaze3d.vertex.ByteBufferBuilder
//#endif

//#if MC >= 1.20.5
//$$ import org.joml.Vector3f
//#endif

//#if MC >= 1.17
import net.minecraft.client.gl.ShaderProgram
import dev.deftu.omnicore.client.OmniClient
//#endif

//#if MC >= 1.16
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC <= 1.19.2
//$$ import com.mojang.math.Matrix4f
//$$ import com.mojang.math.Matrix3f
//#endif

//#if MC <= 1.12.2
//$$ import org.lwjgl.util.vector.Vector4f
//#endif

//#if FABRIC
import net.minecraft.client.render.*
//#else
//#if MC >= 1.16
//$$ import com.mojang.blaze3d.vertex.*
//$$ import net.minecraft.client.renderer.GameRenderer
//$$ import net.minecraft.client.renderer.RenderType
//#else
//$$ import net.minecraft.client.renderer.*
//$$ import net.minecraft.client.renderer.vertex.VertexFormat
//$$ import net.minecraft.client.renderer.vertex.VertexFormatElement
//#endif
//#endif

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import java.util.function.Supplier

@GameSide(Side.CLIENT)
public class OmniTessellator(
    private var buffer: BufferBuilder?,
    //#if MC >= 1.21
    //$$ private var size: Int = 0
    //#endif
) {
    public companion object {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getTessellator(): Tessellator = Tessellator.getInstance()

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getFromBuffer(): OmniTessellator =
            //#if MC >= 1.21
            //$$ OmniTessellator(null)
            //#else
            OmniTessellator(getTessellator().buffer)
            //#endif

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getWithBuffer(buffer: BufferBuilder): OmniTessellator = OmniTessellator(buffer)

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getFromSize(size: Int): OmniTessellator =
            //#if MC >= 1.21
            //$$ OmniTessellator(null, size)
            //#else
            getWithBuffer(BufferBuilder(size))
            //#endif

        //#if MC >= 1.17
        @JvmStatic
        @GameSide(Side.CLIENT)
        public val defaultShaders: IdentityHashMap<VertexFormat, Supplier<ShaderProgram?>> by lazy {
            val value = IdentityHashMap<VertexFormat, Supplier<ShaderProgram?>>()

            //#if MC >= 1.21.4
            //$$ value[net.minecraft.client.render.VertexFormats.POSITION] = Supplier { getProgramFromKey(ShaderProgramKeys.POSITION) }
            //$$ value[net.minecraft.client.render.VertexFormats.POSITION_COLOR] = Supplier { getProgramFromKey(ShaderProgramKeys.POSITION_COLOR) }
            //$$ value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE] = Supplier { getProgramFromKey(ShaderProgramKeys.POSITION_TEX) }
            //$$ value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR] = Supplier { getProgramFromKey(ShaderProgramKeys.POSITION_TEX_COLOR) }
            //$$ value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_LIGHT] = Supplier { getProgramFromKey(ShaderProgramKeys.POSITION_COLOR_TEX_LIGHTMAP) }
            //$$ value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE_LIGHT] = Supplier { getProgramFromKey(ShaderProgramKeys.POSITION_COLOR_TEX_LIGHTMAP) }
            //$$ value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_LIGHT] = Supplier { getProgramFromKey(ShaderProgramKeys.POSITION_COLOR_LIGHTMAP) }
            //$$ value[net.minecraft.client.render.VertexFormats.LINES] = Supplier { getProgramFromKey(ShaderProgramKeys.RENDERTYPE_LINES) }
            //$$ value[net.minecraft.client.render.VertexFormats.BLIT_SCREEN] = Supplier { getProgramFromKey(ShaderProgramKeys.BLIT_SCREEN) }
            //#else
            value[net.minecraft.client.render.VertexFormats.POSITION] = Supplier { GameRenderer.getPositionProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR] = Supplier { GameRenderer.getPositionColorProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE] = Supplier { GameRenderer.getPositionTexProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR] = Supplier { GameRenderer.getPositionTexColorProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_LIGHT] = Supplier { GameRenderer.getParticleProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE_LIGHT] = Supplier { GameRenderer.getPositionColorTexLightmapProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_LIGHT] = Supplier { GameRenderer.getPositionColorLightmapProgram() }
            value[net.minecraft.client.render.VertexFormats.LINES] = Supplier { GameRenderer.getRenderTypeLinesProgram() }
            value[net.minecraft.client.render.VertexFormats.BLIT_SCREEN] = Supplier { OmniClient.getInstance().gameRenderer.blitScreenProgram }

            //#if MC < 1.20.5
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_LIGHT_COLOR] = Supplier { GameRenderer.getPositionTexLightmapColorProgram() }
            value[net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR_NORMAL] = Supplier { GameRenderer.getPositionTexColorNormalProgram() }
            //#endif

            //#if MC < 1.20.1
            value[net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE] = Supplier { GameRenderer.getPositionColorTexProgram() }
            //#endif
            //#endif

            value
        }

        //#if MC >= 1.21.4
        //$$ private fun getProgramFromKey(key: ShaderProgramKey): ShaderProgram? {
        //$$     return OmniClient.getInstance().shaderLoader.getOrCreateProgram(key)
        //$$ }
        //#endif
        //#endif

    }

    private var currentVertexFormat: VertexFormat? = null
    //#if MC >= 1.16
    private var renderLayer: RenderLayer? = null
    //#endif

    @GameSide(Side.CLIENT)
    public fun beginWithActiveShader(mode: DrawModes, format: VertexFormat): OmniTessellator = apply {
        currentVertexFormat = format
        //#if MC >= 1.21
        //$$ buffer = if (size == 0) {
        //$$     getTessellator().begin(mode.vanilla, format)
        //$$ } else {
        //$$     BufferBuilder(ByteBufferBuilder(size), mode.vanilla, format)
        //$$ }
        //#else
        buffer!!.begin(mode.vanilla, format)
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun beginWithActiveShader(mode: DrawModes, format: VertexFormats): OmniTessellator = apply {
        beginWithActiveShader(mode, format.vanilla)
    }

    @GameSide(Side.CLIENT)
    public fun beginWithDefaultShader(mode: DrawModes, format: VertexFormat): OmniTessellator = apply {
        //#if MC >= 1.17
        val supplier = defaultShaders[format] ?: error("Unsupported vertex format '$format' - no default shader")
        OmniRenderState.setShader(supplier)
        //#endif
        beginWithActiveShader(mode, format)
    }

    @GameSide(Side.CLIENT)
    public fun beginWithDefaultShader(mode: DrawModes, format: VertexFormats): OmniTessellator = apply {
        beginWithDefaultShader(mode, format.vanilla)
    }

    //#if MC >= 1.16
    @GameSide(Side.CLIENT)
    public fun beginRenderLayer(layer: RenderLayer): OmniTessellator = apply {
        renderLayer = layer
        beginWithActiveShader(DrawModes.fromRenderLayer(layer), layer.vertexFormat)
    }
    //#endif

    @GameSide(Side.CLIENT)
    public fun draw() {
        checkBuffer()

        //#if MC >= 1.21
        //$$ val builtBuffer = buffer!!.buildOrThrow()
        //#endif
        //#if MC >= 1.16
        if (renderLayer != null) {
            //#if MC >= 1.21
            //$$ renderLayer!!.draw(builtBuffer)
            //#elseif MC >= 1.20
            renderLayer!!.draw(buffer, RenderSystem.getVertexSorting())
            //#else
            //$$ renderLayer!!.draw(buffer, 0, 0, 0)
            //#endif
            return
        }

        //#endif
        handleDraw(
            //#if MC >= 1.21
            //$$ builtBuffer
            //#endif
        )
    }

    private fun handleDraw(
        //#if MC >= 1.21
        //$$ builtBuffer: MeshData
        //#endif
    ) {
        checkBuffer()

        if (currentVertexFormat == null) {
            drawBuffer(
                //#if MC >= 1.21
                //$$ builtBuffer
                //#endif
            )

            return
        }

        //#if MC < 1.17
        //$$ val wantEnabledStates = getDesiredTextureUnitState(currentVertexFormat!!)
        //$$ val wasEnabledStates = BooleanArray(wantEnabledStates.size)
        //$$ for (i in wasEnabledStates.indices) {
        //$$     OmniTextureManager.configureTextureUnit(i) {
        //$$         val isEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D).also { wasEnabledStates[i] = it }
        //$$         val wantEnabled = wantEnabledStates[i]
        //$$         if (isEnabled != wantEnabled) {
        //$$             if (wantEnabled) {
        //$$                 OmniRenderState.enableTexture2D()
        //$$             } else {
        //$$                 OmniRenderState.disableTexture2D()
        //$$             }
        //$$         }
        //$$     }
        //$$ }
        //#endif

        drawBuffer(
            //#if MC >= 1.21
            //$$ builtBuffer
            //#endif
        )

        //#if MC < 1.17
        //$$ for (i in wasEnabledStates.indices) {
        //$$     if (wasEnabledStates[i] == wantEnabledStates[i]) {
        //$$         continue
        //$$     }
        //$$
        //$$     if (wasEnabledStates[i]) {
        //$$         OmniTextureManager.configureTextureUnit(i, OmniRenderState::enableTexture2D)
        //$$     } else {
        //$$         OmniTextureManager.configureTextureUnit(i, OmniRenderState::disableTexture2D)
        //$$     }
        //$$ }
        //#endif
    }

    private fun drawBuffer(
        //#if MC >= 1.21
        //$$ builtBuffer: MeshData
        //#endif
    ) {
        checkBuffer()

        //#if MC >= 1.21
        //$$ BufferUploader.drawWithShader(builtBuffer)
        //#elseif MC >= 1.16.5
        if (buffer == getTessellator().buffer) {
            getTessellator().draw()
        } else {
            //#if MC >= 1.19.2
            BufferRenderer.drawWithGlobalProgram(buffer!!.end())
            //#else
            //$$ BufferRenderer.draw(buffer)
            //#endif
        }
        //#else
        //$$ getTessellator().draw()
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun vertex(
        stack: OmniMatrixStack,
        x: Float,
        y: Float,
        z: Float
    ): OmniTessellator = apply {
        checkBuffer()

        //#if MC >= 1.16
        buffer!!.vertex(stack.peek().matrix, x, y, z)
        //#else
        //$$ val vec = Vector4f(x, y, z, 1f)
        //#if MC >= 1.14
        //$$ vec.transform(stack.peek().matrix)
        //#else
        //$$ Matrix4f.transform(stack.peek().matrix, vec, vec)
        //#endif
        //#if MC >= 1.14
        //$$ buffer!!.vertex(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
        //#else
        //$$ buffer!!.pos(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
        //#endif
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun normal(
        stack: OmniMatrixStack,
        x: Float,
        y: Float,
        z: Float
    ): OmniTessellator = apply {
        checkBuffer()

        //#if MC >= 1.20.5
        //$$ val normal = stack.peek().normal.transform(x, y, z, Vector3f())
        //$$ buffer!!.normal(normal.x, normal.y, normal.z)
        //#elseif MC >= 1.16.2
        buffer!!.normal(stack.peek().normal, x, y, z)
        //#else
        //$$ val vec = org.lwjgl.util.vector.Vector3f(x, y, z)
        //#if MC >= 1.14
        //$$ vec.transform(stack.peek().normal)
        //#else
        //$$ Matrix3f.transform(stack.peek().normal, vec, vec)
        //#endif
        //$$ buffer!!.normal(vec.x, vec.y, vec.z)
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun color(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ): OmniTessellator = apply {
        checkBuffer()

        buffer!!.color(red, green, blue, alpha)
    }

    @GameSide(Side.CLIENT)
    public fun color(
        red: Int,
        green: Int,
        blue: Int,
        alpha: Int
    ): OmniTessellator = color(
        red.toFloat(),
        green.toFloat(),
        blue.toFloat(),
        alpha.toFloat()
    )

    @GameSide(Side.CLIENT)
    public fun color(
        color: Int
    ): OmniTessellator = color(
        (color shr 16 and 255) / 255.0f,
        (color shr 8 and 255) / 255.0f,
        (color and 255) / 255.0f,
        (color shr 24 and 255) / 255.0f
    )

    @GameSide(Side.CLIENT)
    public fun color(
        color: Color
    ): OmniTessellator = color(
        color.red / 255f,
        color.green / 255f,
        color.blue / 255f,
        color.alpha / 255f
    )

    @GameSide(Side.CLIENT)
    public fun texture(
        u: Float,
        v: Float
    ): OmniTessellator = apply {
        checkBuffer()

        //#if MC >= 1.16.5
        buffer!!.texture(u, v)
        //#else
        //$$ buffer!!.tex(u.toDouble(), v.toDouble())
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun overlay(
        u: Int,
        v: Int
    ): OmniTessellator = apply {
        checkBuffer()

        //#if MC >= 1.16.5
        buffer!!.overlay(u, v)
        //#else
        //$$ buffer!!.tex(u.toDouble(), v.toDouble())
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun light(
        u: Int,
        v: Int
    ): OmniTessellator = apply {
        checkBuffer()

        //#if MC >= 1.16.5
        buffer!!.light(u, v)
        //#else
        //$$ buffer!!.lightmap(u, v)
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun next(): OmniTessellator = apply {
        checkBuffer()

        //#if MC < 1.21
        //#if MC >= 1.16.5
        buffer!!.next()
        //#else
        //$$ buffer!!.endVertex()
        //#endif
        //#endif
    }

    private fun checkBuffer() {
        checkNotNull(buffer) { "BufferBuilder is null" }
    }

    //#if MC < 1.17.1
    //$$ private fun getDesiredTextureUnitState(vertexFormat: VertexFormat): BooleanArray {
    //$$     var wantEnabled = BooleanArray(2)
    //$$     for (element in vertexFormat.elements) {
    //$$         //#if MC >= 1.16.5
    //$$         if (element.type === VertexFormatElement.Type.UV) {
    //$$             val index: Int = element.textureIndex
    //$$         //#else
    //$$         //$$ if (element.usage == VertexFormatElement.EnumUsage.UV) {
    //$$         //$$     val index: Int = element.index
    //$$         //#endif
    //$$             if (wantEnabled.size <= index)
    //$$                 wantEnabled = wantEnabled.copyOf(index + 1)
    //$$             wantEnabled[index] = true
    //$$         }
    //$$     }
    //$$
    //$$     return wantEnabled
    //$$ }
    //#endif

    @GameSide(Side.CLIENT)
    public enum class VertexFormats(
        public val vanilla: VertexFormat
    ) {
        POSITION(net.minecraft.client.render.VertexFormats.POSITION),
        POSITION_COLOR(net.minecraft.client.render.VertexFormats.POSITION_COLOR),
        POSITION_TEXTURE(net.minecraft.client.render.VertexFormats.POSITION_TEXTURE),
        POSITION_TEXTURE_COLOR(net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR)
    }

    @GameSide(Side.CLIENT)
    public enum class DrawModes(
        public val gl: Int
    ) {
        LINES(GL11.GL_LINES),
        LINE_STRIP(GL11.GL_LINE_STRIP),
        TRIANGLES(GL11.GL_TRIANGLES),
        TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
        TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
        QUADS(GL11.GL_QUADS);

        //#if MC >= 1.17
        public val vanilla: VertexFormat.DrawMode
        //#else
        //$$ public val vanilla: Int
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

        public companion object {

            @JvmStatic
            @GameSide(Side.CLIENT)
            public fun fromGl(gl: Int): DrawModes {
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
            @JvmStatic
            @GameSide(Side.CLIENT)
            public fun glToVanillaDrawMode(glMode: Int): VertexFormat.DrawMode {
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

            @JvmStatic
            @GameSide(Side.CLIENT)
            public fun fromMc(mcMode: VertexFormat.DrawMode): DrawModes {
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
            @JvmStatic
            @GameSide(Side.CLIENT)
            public fun fromRenderLayer(layer: RenderLayer): DrawModes {
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

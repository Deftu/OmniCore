@file:Suppress("MemberVisibilityCanBePrivate")

package dev.deftu.omnicore.client.render.pipeline

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.lwjgl.opengl.GL11

//#if MC >= 1.17.1
import com.mojang.blaze3d.vertex.VertexFormat
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.render.RenderLayer
//#endif

@Suppress("JoinDeclarationAndAssignment")
@GameSide(Side.CLIENT)
public enum class DrawModes(
    public val glId: Int
) {

    LINES(GL11.GL_LINES),
    LINE_STRIP(GL11.GL_LINE_STRIP),
    TRIANGLES(GL11.GL_TRIANGLES),
    TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
    TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
    QUADS(GL11.GL_QUADS);

    //#if MC >= 1.17.1
    public val vanilla: VertexFormat.DrawMode
    //#else
    //$$ public val vanilla: Int
    //#endif

    init {
        //#if MC >= 1.17.1
        vanilla = when (glId) {
            GL11.GL_LINES -> VertexFormat.DrawMode.LINES
            GL11.GL_LINE_STRIP -> VertexFormat.DrawMode.LINE_STRIP
            GL11.GL_TRIANGLES -> VertexFormat.DrawMode.TRIANGLES
            GL11.GL_TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP
            GL11.GL_TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN
            GL11.GL_QUADS -> VertexFormat.DrawMode.QUADS
            else -> throw IllegalArgumentException("Unsupported draw mode $glId")
        }
        //#else
        //$$ vanilla = glId
        //#endif
    }

    public companion object {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun fromGl(glId: Int): DrawModes {
            return when (glId) {
                GL11.GL_LINES -> LINES
                GL11.GL_LINE_STRIP -> LINE_STRIP
                GL11.GL_TRIANGLES -> TRIANGLES
                GL11.GL_TRIANGLE_STRIP -> TRIANGLE_STRIP
                GL11.GL_TRIANGLE_FAN -> TRIANGLE_FAN
                GL11.GL_QUADS -> QUADS
                else -> throw IllegalArgumentException("Unsupported draw mode $glId")
            }
        }

        //#if MC >= 1.17.1
        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun glToVanillaDrawMode(glId: Int): VertexFormat.DrawMode {
            return when (glId) {
                GL11.GL_LINES -> VertexFormat.DrawMode.LINES
                GL11.GL_LINE_STRIP -> VertexFormat.DrawMode.LINE_STRIP
                GL11.GL_TRIANGLES -> VertexFormat.DrawMode.TRIANGLES
                GL11.GL_TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP
                GL11.GL_TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN
                GL11.GL_QUADS -> VertexFormat.DrawMode.QUADS
                else -> throw IllegalArgumentException("Unsupported draw mode $glId")
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

        //#if MC >= 1.16.5
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

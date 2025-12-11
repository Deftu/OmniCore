package dev.deftu.omnicore.api.client.render

import org.lwjgl.opengl.GL11

//#if MC >= 1.17.1
import com.mojang.blaze3d.vertex.VertexFormat
//#endif

public enum class DrawMode {
    LINES,
    @Deprecated("No longer available as of Minecraft 1.21.11", replaceWith = ReplaceWith("LINES"))
    LINE_STRIP,
    TRIANGLES,
    TRIANGLE_STRIP,
    TRIANGLE_FAN,
    QUADS;

    public val const: Int
        get() {
            return when (this) {
                LINES -> GL11.GL_LINES
                LINE_STRIP -> GL11.GL_LINE_STRIP
                TRIANGLES -> GL11.GL_TRIANGLES
                TRIANGLE_STRIP -> GL11.GL_TRIANGLE_STRIP
                TRIANGLE_FAN -> GL11.GL_TRIANGLE_FAN
                QUADS -> GL11.GL_QUADS
            }
        }

    //#if MC >= 1.17.1
    public val vanilla: VertexFormat.Mode
        get() {
            return when (const) {
                GL11.GL_LINES -> VertexFormat.Mode.LINES
                //#if MC < 1.21.11
                GL11.GL_LINE_STRIP -> VertexFormat.Mode.LINE_STRIP
                //#endif
                GL11.GL_TRIANGLES -> VertexFormat.Mode.TRIANGLES
                GL11.GL_TRIANGLE_STRIP -> VertexFormat.Mode.TRIANGLE_STRIP
                GL11.GL_TRIANGLE_FAN -> VertexFormat.Mode.TRIANGLE_FAN
                GL11.GL_QUADS -> VertexFormat.Mode.QUADS
                else -> DrawModes.die(const)
            }
        }
    //#else
    //$$ public val vanilla: Int
    //$$     get() = const
    //#endif
}

package dev.deftu.omnicore.api.client.render

import org.lwjgl.opengl.GL11

//#if MC >= 1.17.1
import com.mojang.blaze3d.vertex.VertexFormat
//#endif

public enum class DrawMode {
    LINES,
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

    @Suppress("JoinDeclarationAndAssignment")
    //#if MC >= 1.17.1
    public val vanilla: VertexFormat.DrawMode
    //#else
    //$$ public val vanilla: Int
    //#endif

    init {
        //#if MC >= 1.17.1
        vanilla = when (const) {
            GL11.GL_LINES -> VertexFormat.DrawMode.LINES
            GL11.GL_LINE_STRIP -> VertexFormat.DrawMode.LINE_STRIP
            GL11.GL_TRIANGLES -> VertexFormat.DrawMode.TRIANGLES
            GL11.GL_TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP
            GL11.GL_TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN
            GL11.GL_QUADS -> VertexFormat.DrawMode.QUADS
            else -> DrawModes.die(const)
        }
        //#else
        //$$ vanilla = const
        //#endif
    }
}

package dev.deftu.omnicore.api.client.render

import org.lwjgl.opengl.GL11

//#if MC >= 1.17.1
import com.mojang.blaze3d.vertex.VertexFormat
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.render.RenderLayer
//#endif

public object DrawModes {
    @JvmStatic
    public fun from(const: Int): DrawMode {
        return when (const) {
            GL11.GL_LINES -> DrawMode.LINES
            GL11.GL_LINE_STRIP -> DrawMode.LINE_STRIP
            GL11.GL_TRIANGLES -> DrawMode.TRIANGLES
            GL11.GL_TRIANGLE_STRIP -> DrawMode.TRIANGLE_STRIP
            GL11.GL_TRIANGLE_FAN -> DrawMode.TRIANGLE_FAN
            GL11.GL_QUADS -> DrawMode.QUADS
            else -> die(const)
        }
    }

    //#if MC >= 1.16.5
    @JvmStatic
    public fun from(layer: RenderLayer): DrawMode {
        return from(layer.drawMode)
    }
    //#endif

    //#if MC >= 1.17.1
    @JvmStatic
    public fun from(vanilla: VertexFormat.DrawMode): DrawMode {
        return when (vanilla) {
            VertexFormat.DrawMode.LINES -> DrawMode.LINES
            VertexFormat.DrawMode.LINE_STRIP -> DrawMode.LINE_STRIP
            VertexFormat.DrawMode.TRIANGLES -> DrawMode.TRIANGLES
            VertexFormat.DrawMode.TRIANGLE_STRIP -> DrawMode.TRIANGLE_STRIP
            VertexFormat.DrawMode.TRIANGLE_FAN -> DrawMode.TRIANGLE_FAN
            VertexFormat.DrawMode.QUADS -> DrawMode.QUADS
            else -> die(vanilla)
        }
    }

    @JvmStatic
    public fun vanilla(const: Int): VertexFormat.DrawMode {
        return when (const) {
            GL11.GL_LINES -> VertexFormat.DrawMode.LINES
            GL11.GL_LINE_STRIP -> VertexFormat.DrawMode.LINE_STRIP
            GL11.GL_TRIANGLES -> VertexFormat.DrawMode.TRIANGLES
            GL11.GL_TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP
            GL11.GL_TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN
            GL11.GL_QUADS -> VertexFormat.DrawMode.QUADS
            else -> die(const)
        }
    }
    //#endif

    @Throws(IllegalArgumentException::class)
    internal fun die(what: Any): Nothing {
        throw IllegalArgumentException("Unsupported draw mode $what")
    }
}

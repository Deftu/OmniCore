package dev.deftu.omnicore.api.client.render

import org.lwjgl.opengl.GL11

//#if MC >= 1.17.1
import com.mojang.blaze3d.vertex.VertexFormat
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.renderer.RenderType
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
    public fun from(layer: RenderType): DrawMode {
        return from(layer.mode())
    }
    //#endif

    //#if MC >= 1.17.1
    @JvmStatic
    public fun from(vanilla: VertexFormat.Mode): DrawMode {
        return when (vanilla) {
            VertexFormat.Mode.LINES -> DrawMode.LINES
            VertexFormat.Mode.LINE_STRIP -> DrawMode.LINE_STRIP
            VertexFormat.Mode.TRIANGLES -> DrawMode.TRIANGLES
            VertexFormat.Mode.TRIANGLE_STRIP -> DrawMode.TRIANGLE_STRIP
            VertexFormat.Mode.TRIANGLE_FAN -> DrawMode.TRIANGLE_FAN
            VertexFormat.Mode.QUADS -> DrawMode.QUADS
            else -> die(vanilla)
        }
    }

    @JvmStatic
    public fun vanilla(const: Int): VertexFormat.Mode {
        return when (const) {
            GL11.GL_LINES -> VertexFormat.Mode.LINES
            GL11.GL_LINE_STRIP -> VertexFormat.Mode.LINE_STRIP
            GL11.GL_TRIANGLES -> VertexFormat.Mode.TRIANGLES
            GL11.GL_TRIANGLE_STRIP -> VertexFormat.Mode.TRIANGLE_STRIP
            GL11.GL_TRIANGLE_FAN -> VertexFormat.Mode.TRIANGLE_FAN
            GL11.GL_QUADS -> VertexFormat.Mode.QUADS
            else -> die(const)
        }
    }
    //#endif

    @Throws(IllegalArgumentException::class)
    internal fun die(what: Any): Nothing {
        throw IllegalArgumentException("Unsupported draw mode $what")
    }
}

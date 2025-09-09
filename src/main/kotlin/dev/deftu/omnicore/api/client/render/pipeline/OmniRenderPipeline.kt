package dev.deftu.omnicore.api.client.render.pipeline

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.util.Identifier

public interface OmniRenderPipeline {
    public enum class DepthTest {
        DISABLED,
        ALWAYS,
        EQUAL,
        LESS_OR_EQUAL,
        LESS,
        GREATER,
    }

    public enum class ColorLogic {
        NONE,
        OR_REVERSE,
    }

    public val location: Identifier
    public val vertexFormat: VertexFormat

    //#if MC >= 1.21.5
    public val vanilla: RenderPipeline
    //#endif

    public fun bind()
    public fun unbind()
}

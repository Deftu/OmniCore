package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.CoreShaders
import net.minecraft.client.renderer.ShaderProgram
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object DefaultShaders {
    private val defaults: Map<VertexFormat, ShaderProgram> by lazy {
        mapOf(
            DefaultVertexFormat.POSITION to CoreShaders.POSITION,
            DefaultVertexFormat.POSITION_COLOR to CoreShaders.POSITION_COLOR,
            DefaultVertexFormat.POSITION_TEX to CoreShaders.POSITION_TEX,
            DefaultVertexFormat.POSITION_TEX_COLOR to CoreShaders.POSITION_TEX_COLOR,
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP to CoreShaders.POSITION_COLOR_TEX_LIGHTMAP,
            DefaultVertexFormat.POSITION_COLOR_LIGHTMAP to CoreShaders.POSITION_COLOR_LIGHTMAP,
            DefaultVertexFormat.POSITION_COLOR_NORMAL to CoreShaders.RENDERTYPE_LINES,
            DefaultVertexFormat.BLIT_SCREEN to CoreShaders.BLIT_SCREEN,
        )
    }

    @JvmStatic
    public operator fun get(format: VertexFormat): ShaderProgram? {
        return defaults[format]
    }
}

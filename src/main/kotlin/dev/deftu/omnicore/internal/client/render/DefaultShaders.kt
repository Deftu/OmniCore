package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.identifierOrThrow
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier

public object DefaultShaders {
    private val defaults: Map<VertexFormat, Identifier> by lazy {
        mapOf(
            VertexFormats.POSITION to identifierOrThrow("core/position"),
            VertexFormats.POSITION_COLOR to identifierOrThrow("core/position_color"),
            VertexFormats.POSITION_TEXTURE to identifierOrThrow("core/position_tex"),
            VertexFormats.POSITION_TEXTURE_COLOR to identifierOrThrow("core/position_tex_color"),
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT to identifierOrThrow("core/position_color_tex_lightmap"),
            VertexFormats.POSITION_COLOR_LIGHT to identifierOrThrow("core/position_color_lightmap"),
            VertexFormats.POSITION_COLOR_NORMAL to identifierOrThrow("core/rendertype_lines"),
            VertexFormats.BLIT_SCREEN to identifierOrThrow("core/blit_screen"),
        )
    }

    @JvmStatic
    public operator fun get(format: VertexFormat): Identifier? {
        return defaults[format]
    }
}

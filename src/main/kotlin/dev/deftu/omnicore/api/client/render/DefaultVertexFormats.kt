package dev.deftu.omnicore.api.client.render

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.internal.client.render.CustomVertexFormats
import com.mojang.blaze3d.vertex.DefaultVertexFormat

public enum class DefaultVertexFormats(public val vanilla: VertexFormat) {
    POSITION(DefaultVertexFormat.POSITION),
    POSITION_COLOR(DefaultVertexFormat.POSITION_COLOR),
    POSITION_TEXTURE(DefaultVertexFormat.POSITION_TEX),
    POSITION_TEXTURE_COLOR(DefaultVertexFormat.POSITION_TEX_COLOR),
    POSITION_COLOR_NORMAL(CustomVertexFormats.POSITION_COLOR_NORMAL);
}

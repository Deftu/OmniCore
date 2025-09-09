package dev.deftu.omnicore.api.client.render

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.render.VertexFormats

public enum class DefaultVertexFormats(public val vanilla: VertexFormat) {
    POSITION(VertexFormats.POSITION),
    POSITION_COLOR(VertexFormats.POSITION_COLOR),
    POSITION_TEXTURE(VertexFormats.POSITION_TEXTURE),
    POSITION_TEXTURE_COLOR(VertexFormats.POSITION_TEXTURE_COLOR);
}

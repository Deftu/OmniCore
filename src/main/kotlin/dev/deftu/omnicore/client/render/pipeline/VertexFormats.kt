package dev.deftu.omnicore.client.render.pipeline

import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import com.mojang.blaze3d.vertex.VertexFormat

@GameSide(Side.CLIENT)
public enum class VertexFormats(
    public val vanilla: VertexFormat
) {
    POSITION(net.minecraft.client.render.VertexFormats.POSITION),
    POSITION_COLOR(net.minecraft.client.render.VertexFormats.POSITION_COLOR),
    POSITION_TEXTURE(net.minecraft.client.render.VertexFormats.POSITION_TEXTURE),
    POSITION_TEXTURE_COLOR(net.minecraft.client.render.VertexFormats.POSITION_TEXTURE_COLOR)
}

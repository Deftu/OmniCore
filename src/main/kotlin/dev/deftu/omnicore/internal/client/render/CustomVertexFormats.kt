package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.render.VertexFormats
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CustomVertexFormats {
    @JvmField
    public val POSITION_COLOR_NORMAL: VertexFormat = VertexFormats.POSITION_COLOR_NORMAL
}

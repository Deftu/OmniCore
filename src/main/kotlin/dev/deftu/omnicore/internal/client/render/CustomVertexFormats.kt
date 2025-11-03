package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CustomVertexFormats {
    @JvmField
    public val POSITION_COLOR_NORMAL: VertexFormat = DefaultVertexFormat.POSITION_COLOR_NORMAL
}

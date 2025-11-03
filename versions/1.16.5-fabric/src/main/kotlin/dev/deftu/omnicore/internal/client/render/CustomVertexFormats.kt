package dev.deftu.omnicore.internal.client.render

import com.google.common.collect.ImmutableList
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CustomVertexFormats {
    @JvmField
    public val POSITION_COLOR_NORMAL: VertexFormat = VertexFormat(
        ImmutableList.builder<VertexFormatElement>()
            .add(DefaultVertexFormat.ELEMENT_POSITION)
            .add(DefaultVertexFormat.ELEMENT_COLOR)
            .add(DefaultVertexFormat.ELEMENT_NORMAL)
            .add(DefaultVertexFormat.ELEMENT_PADDING)
            .build()
    )
}

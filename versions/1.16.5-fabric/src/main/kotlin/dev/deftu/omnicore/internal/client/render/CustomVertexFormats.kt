package dev.deftu.omnicore.internal.client.render

import com.google.common.collect.ImmutableList
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormatElement
import net.minecraft.client.render.VertexFormats
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CustomVertexFormats {
    @JvmField
    public val POSITION_COLOR_NORMAL: VertexFormat = VertexFormat(
        ImmutableList.builder<VertexFormatElement>()
            .add(VertexFormats.POSITION_ELEMENT)
            .add(VertexFormats.COLOR_ELEMENT)
            .add(VertexFormats.NORMAL_ELEMENT)
            .add(VertexFormats.PADDING_ELEMENT)
            .build()
    )
}

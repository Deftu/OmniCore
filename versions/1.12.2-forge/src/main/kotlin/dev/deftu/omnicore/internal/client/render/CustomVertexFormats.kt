package dev.deftu.omnicore.internal.client.render

import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CustomVertexFormats {
    @JvmField
    public val POSITION_COLOR_NORMAL: VertexFormat = VertexFormat().apply {
        addElement(DefaultVertexFormats.POSITION_3F)
        addElement(DefaultVertexFormats.COLOR_4UB)
        addElement(DefaultVertexFormats.NORMAL_3B)
        addElement(DefaultVertexFormats.PADDING_1B)
    }
}

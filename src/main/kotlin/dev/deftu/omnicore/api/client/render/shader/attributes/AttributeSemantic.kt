package dev.deftu.omnicore.api.client.render.shader.attributes

import com.mojang.blaze3d.vertex.VertexFormatElement

//#if MC < 1.21.1
//$$ import com.mojang.blaze3d.vertex.DefaultVertexFormat
//#endif

public sealed interface AttributeSemantic {
    public val element: VertexFormatElement
        get() = when (this) {
            //#if MC >= 1.21.1
            Position -> VertexFormatElement.POSITION
            Normal -> VertexFormatElement.NORMAL
            is Color -> VertexFormatElement.COLOR
            is UV -> when (index) {
                0 -> VertexFormatElement.UV0
                1 -> VertexFormatElement.UV1
                else -> throw IllegalArgumentException("UV index must be between 0 and 1 inclusive")
            }
            //#else
            //$$ Position -> DefaultVertexFormat.ELEMENT_POSITION
            //$$ Normal -> DefaultVertexFormat.ELEMENT_NORMAL
            //$$ is Color -> DefaultVertexFormat.ELEMENT_COLOR
            //$$ is UV -> when (index) {
            //$$     0 -> DefaultVertexFormat.ELEMENT_UV0
            //$$     1 -> DefaultVertexFormat.ELEMENT_UV1
            //$$     else -> throw IllegalArgumentException("UV index must be between 0 and 1 inclusive")
            //$$ }
            //#endif
        }

    public data object Position : AttributeSemantic
    public data object Normal : AttributeSemantic
    public data object Color : AttributeSemantic

    public data class UV(public val index: Int = 0) : AttributeSemantic
}

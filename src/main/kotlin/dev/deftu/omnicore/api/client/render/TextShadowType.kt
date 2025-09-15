package dev.deftu.omnicore.api.client.render

public sealed interface TextShadowType {
    public data object None : TextShadowType
    public data object Drop : TextShadowType
    public data class Outline(public val color: Int = 0xFF000000.toInt()) : TextShadowType
}

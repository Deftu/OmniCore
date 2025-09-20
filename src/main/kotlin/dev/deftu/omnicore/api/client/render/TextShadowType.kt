package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.color.OmniColors

public sealed interface TextShadowType {
    public data object None : TextShadowType
    public data object Drop : TextShadowType
    public data class Outline(public val color: OmniColor = OmniColors.BLACK) : TextShadowType
}

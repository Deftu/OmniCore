package dev.deftu.omnicore.api.client.input

public object OmniMouseButtons {
    @JvmField public val LEFT: OmniMouseButton = OmniMouseButton(0, "Left")
    @JvmField public val RIGHT: OmniMouseButton = OmniMouseButton(1, "Right")
    @JvmField public val MIDDLE: OmniMouseButton = OmniMouseButton(2, "Middle")
    @JvmField public val BACK: OmniMouseButton = OmniMouseButton(3, "Side Back")
    @JvmField public val FORWARD: OmniMouseButton = OmniMouseButton(4, "Side Forward")
    @JvmField public val BUTTON6: OmniMouseButton = OmniMouseButton(5, "Button6")
    @JvmField public val BUTTON7: OmniMouseButton = OmniMouseButton(6, "Button7")
    @JvmField public val BUTTON8: OmniMouseButton = OmniMouseButton(7, "Button8")

    @JvmField public  val ALL: List<OmniMouseButton> = listOf(LEFT, RIGHT, MIDDLE, BACK, FORWARD, BUTTON6, BUTTON7, BUTTON8)
    @JvmField public val BY_CODE: Map<Int, OmniMouseButton> = ALL.associateBy(OmniMouseButton::code)

    @JvmStatic
    public fun from(code: Int): OmniMouseButton {
        return BY_CODE[code] ?: OmniMouseButton(code, "Unknown")
    }

    @JvmStatic
    public fun name(code: Int): String {
        return from(code).name
    }
}

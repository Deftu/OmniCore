package dev.deftu.omnicore.api.color

import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public enum class NamedColor(public val id: String, public val color: OmniColor) {
    TRANSPARENT("transparent", OmniColors.TRANSPARENT),
    WHITE("white", OmniColors.WHITE),
    LIGHT_GRAY("light_gray", OmniColors.LIGHT_GRAY),
    GRAY("gray", OmniColors.GRAY),
    DARK_GRAY("dark_gray", OmniColors.DARK_GRAY),
    BLACK("black", OmniColors.BLACK),
    RED("red", OmniColors.RED),
    PINK("pink", OmniColors.PINK),
    ORANGE("orange", OmniColors.ORANGE),
    YELLOW("yellow", OmniColors.YELLOW),
    GREEN("green", OmniColors.GREEN),
    LIME("lime", OmniColors.LIME),
    MAGENTA("magenta", OmniColors.MAGENTA),
    CYAN("cyan", OmniColors.CYAN),
    BLUE("blue", OmniColors.BLUE);

    override fun toString(): String {
        return "[$id]($color)"
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<NamedColor> = values().toList()

        @JvmStatic
        public fun complete(input: String): List<NamedColor> {
            return ALL.filter { it.id.startsWith(input.lowercase()) }
        }

        @JvmStatic
        public fun from(id: String): NamedColor? {
            return ALL.firstOrNull { it.id.equals(id, ignoreCase = true) }
        }
    }
}

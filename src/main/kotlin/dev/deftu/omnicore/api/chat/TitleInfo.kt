package dev.deftu.omnicore.api.chat

import dev.deftu.textile.Text

public data class TitleInfo(
    public val title: Text,
    public val subtitle: Text?,
    public val timings: Timings = Timings.DEFAULT,
) {
    public data class Timings(
        val fadeIn: Int,
        val stay: Int,
        val fadeOut: Int
    ) {
        public companion object {
            public const val DEFAULT_FADE_IN: Int = 10
            public const val DEFAULT_STAY: Int = 70
            public const val DEFAULT_FADE_OUT: Int = 20

            @JvmField
            public val DEFAULT: Timings = Timings(
                fadeIn = DEFAULT_FADE_IN,
                stay = DEFAULT_STAY,
                fadeOut = DEFAULT_FADE_OUT
            )
        }

        public fun withFadeIn(fadeIn: Int): Timings {
            return if (this.fadeIn == fadeIn) this else copy(fadeIn = fadeIn)
        }

        public fun withStay(stay: Int): Timings {
            return if (this.stay == stay) this else copy(stay = stay)
        }

        public fun withFadeOut(fadeOut: Int): Timings {
            return if (this.fadeOut == fadeOut) this else copy(fadeOut = fadeOut)
        }
    }

    public companion object {
        @JvmStatic
        public fun of(title: Text, subtitle: Text? = null): TitleInfo {
            return TitleInfo(title, subtitle)
        }

        @JvmStatic
        public fun of(title: String, subtitle: String? = null): TitleInfo {
            return TitleInfo(Text.literal(title), subtitle?.let(Text::literal))
        }

        @JvmStatic
        public fun of(
            title: Text,
            subtitle: Text?,
            fadeIn: Int,
            stay: Int,
            fadeOut: Int
        ): TitleInfo {
            return TitleInfo(
                title,
                subtitle,
                Timings(fadeIn, stay, fadeOut)
            )
        }

        @JvmStatic
        public fun of(
            title: String,
            subtitle: String?,
            fadeIn: Int,
            stay: Int,
            fadeOut: Int
        ): TitleInfo {
            return TitleInfo(
                Text.literal(title),
                subtitle?.let(Text::literal),
                Timings(fadeIn, stay, fadeOut)
            )
        }
    }

    public fun withTimings(timings: Timings): TitleInfo {
        return if (this.timings == timings) this else copy(timings = timings)
    }
}

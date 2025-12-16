package dev.deftu.omnicore.api.time

public data class TickDurationFormat(
    public val style: Style = Style.Compact,
    public val millisVisible: Boolean = false,
    public val maxUnits: Int = 3,
) {
    public companion object {
        @JvmField public val DEFAULT: TickDurationFormat = TickDurationFormat()
        @JvmField public val HH_MM_SS: TickDurationFormat = DEFAULT.withStyle(Style.Clock(Style.Clock.Type.HH_MM_SS))
        @JvmField public val MM_SS: TickDurationFormat = DEFAULT.withStyle(Style.Clock(Style.Clock.Type.MM_SS))
        @JvmField public val SS: TickDurationFormat = DEFAULT.withStyle(Style.Clock(Style.Clock.Type.SS))
    }

    public sealed interface Style {
        public object Compact : Style {
            override fun format(hours: Long, minutes: Int, seconds: Int, millis: Int, millisVisible: Boolean, maxUnits: Int): String {
                val clampedMaxUnits = maxUnits.coerceAtLeast(1)

                return buildString {
                    var units = 0

                    fun appendUnit(value: Long, suffix: String) {
                        if (units > 0) append(' ')
                        append(value).append(suffix)
                        units++
                    }

                    if (hours > 0 && units < clampedMaxUnits) appendUnit(hours, "h")
                    if (minutes > 0 && units < clampedMaxUnits) appendUnit(minutes.toLong(), "m")
                    if ((seconds > 0 || units == 0) && units < clampedMaxUnits) appendUnit(seconds.toLong(), "s")

                    if (millisVisible && units < clampedMaxUnits) {
                        appendUnit(millis.toLong(), "ms")
                    }
                }
            }
        }

        public data class Clock(public val type: Type) : Style {
            public enum class Type { HH_MM_SS, MM_SS, SS }

            override fun format(hours: Long, minutes: Int, seconds: Int, millis: Int, millisVisible: Boolean, maxUnits: Int): String {
                return buildString {
                    when (type) {
                        Type.HH_MM_SS -> {
                            append(hours) // do not clamp/pad hours
                            append(':')
                            appendTwoDigits(minutes)
                            append(':')
                            appendTwoDigits(seconds)
                        }

                        Type.MM_SS -> {
                            val totalMinutes = hours * 60L + minutes.toLong()
                            append(totalMinutes) // do not clamp minutes
                            append(':')
                            appendTwoDigits(seconds)
                        }

                        Type.SS -> {
                            val totalSeconds = hours * 3600L + minutes.toLong() * 60L + seconds.toLong()
                            append(totalSeconds)
                        }
                    }

                    if (millisVisible) {
                        append('.')
                        appendThreeDigits(millis)
                    }
                }
            }

            private fun StringBuilder.appendTwoDigits(value: Int) {
                val v = value.coerceIn(0, 59) // minutes/seconds should be within 0..59 here
                if (v < 10) append('0')
                append(v)
            }

            private fun StringBuilder.appendThreeDigits(value: Int) {
                val v = value.coerceIn(0, 999)
                if (v < 100) append('0')
                if (v < 10) append('0')
                append(v)
            }
        }

        public fun format(hours: Long, minutes: Int, seconds: Int, millis: Int, millisVisible: Boolean, maxUnits: Int): String
    }

    public fun format(ticks: Long): String {
        return formatMillis(TickTime.ticksToMillis(ticks))
    }

    public fun formatMillis(millis: Long): String {
        val millis = millis.coerceAtLeast(0)
        val totalSeconds = millis / 1000L
        val remainingMillis = (millis % 1000L).toInt()

        val seconds = (totalSeconds % 60).toInt()
        val totalMinutes = totalSeconds / 60L
        val minutes = (totalMinutes % 60).toInt()
        val hours = totalMinutes / 60L

        return style.format(hours, minutes, seconds, remainingMillis, millisVisible, maxUnits)
    }

    public fun withStyle(style: Style): TickDurationFormat {
        return if (this.style == style) this else copy(style = style)
    }

    public fun withMillisVisible(millisVisible: Boolean): TickDurationFormat {
        return if (this.millisVisible == millisVisible) this else copy(millisVisible = millisVisible)
    }

    public fun withMaxUnits(maxUnits: Int): TickDurationFormat {
        return if (this.maxUnits == maxUnits) this else copy(maxUnits = maxUnits)
    }
}

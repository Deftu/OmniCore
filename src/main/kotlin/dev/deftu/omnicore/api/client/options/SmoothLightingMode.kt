package dev.deftu.omnicore.api.client.options

public enum class SmoothLightingMode {
    OFF,
    MINIMUM,
    MAXIMUM;

    public companion object {
        public fun from(vanilla: Int): SmoothLightingMode {
            return when (vanilla) {
                0 -> OFF
                1 -> MINIMUM
                2 -> MAXIMUM
                else -> throw IllegalArgumentException("Unknown smooth lighting mode: $vanilla")
            }
        }
    }
}

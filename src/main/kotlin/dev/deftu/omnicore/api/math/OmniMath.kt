package dev.deftu.omnicore.api.math

public object OmniMath {
    @JvmStatic
    public fun lerp(a: Double, b: Double, delta: Double): Double {
        return a + (b - a) * delta
    }

    @JvmStatic
    public fun lerp(a: Float, b: Float, delta: Float): Float {
        return a + (b - a) * delta
    }

    @JvmStatic
    public fun lerp(a: Int, b: Int, delta: Float): Int {
        return (a + (b - a) * delta).toInt()
    }
}
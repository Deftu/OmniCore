package dev.deftu.omnicore.api.client.render

import org.intellij.lang.annotations.MagicConstant
import org.lwjgl.opengl.GL11

/**
 * A value class representing a mask for clearing buffers in OpenGL.
 *
 * This class encapsulates the bitwise flags used in the `glClear` function to specify which
 * buffers to clear. It provides predefined constants for common buffer types and combinations,
 * as well as methods for combining masks and checking for specific flags.
 *
 * @property bits The integer value representing the combined bitwise flags.
 */
@JvmInline
public value class ClearMask private constructor(public val bits: Int) {
    public infix fun or(other: ClearMask): ClearMask {
        return ClearMask(bits or other.bits)
    }

    public fun has(flag: ClearMask): Boolean {
        return (bits and flag.bits) != 0
    }

    public companion object {
        // Core flags
        public val NONE: ClearMask = ClearMask(0)
        public val COLOR: ClearMask = ClearMask(GL11.GL_COLOR_BUFFER_BIT)
        public val DEPTH: ClearMask = ClearMask(GL11.GL_DEPTH_BUFFER_BIT)
        public val STENCIL: ClearMask = ClearMask(GL11.GL_STENCIL_BUFFER_BIT)

        // Combinations
        public val COLOR_DEPTH: ClearMask = COLOR or DEPTH
        public val ALL: ClearMask = COLOR or DEPTH or STENCIL

        public fun from(
            @MagicConstant(flagsFromClass = GL11::class)
            bits: Int
        ): ClearMask {
            return ClearMask(bits)
        }
    }
}
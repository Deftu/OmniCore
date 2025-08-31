package dev.deftu.omnicore.api.client.textures

import java.awt.Color

public interface OmniTextureHandle : AutoCloseable {
    public data class CopyOp(
        val source: OmniTextureHandle,
        val sourceX: Int,
        val sourceY: Int,
        val destX: Int,
        val destY: Int,
        val width: Int,
        val height: Int,
    )

    /** The backing GL texture ID for this texture. */
    public val id: Int
    public val width: Int
    public val height: Int
    public val format: OmniTextureFormat

    /** Resizes the texture to the given width and height. */
    public fun resize(width: Int, height: Int)

    /** Clears the texture to the given color values. */
    public fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)

    /** Clears the texture to the given depth value. */
    public fun clearDepth(depth: Float)

    /** Provides an array of color values for the given region of the texture. */
    public fun readColor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ): Array<Color>

    /** Provides a single color value for the given pixel in the texture. */
    public fun readColor(x: Int, y: Int): Color

    /** Provides an array of depth values for the given region of the texture. */
    public fun readDepth(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ): Array<Float>

    /** Provides a single depth value for the given pixel in the texture. */
    public fun readDepth(x: Int, y: Int): Float

    /**
     * Performs the provided copy operations from the sources in said options to this texture.
     *
     * This function makes the assumption that the source textures are the same size as this texture and the coordinates given are within the bounds of this texture.
     */
    public fun copyFrom(operations: Iterable<CopyOp>)

    /**
     * Performs the provided copy operation from its source texture to this texture.
     *
     * See [copyFrom] for more details.
     */
    public fun copyFrom(operation: CopyOp) {
        copyFrom(setOf(operation))
    }

    /**
     * Copies the entirety of the given texture to this texture.
     *
     * If the sizes of the textures differ, this texture will be resized to match the source texture automatically.
     */
    public fun copyFrom(src: OmniTextureHandle) {
        if (width != src.width || height != src.height) {
            resize(src.width, src.height)
        }

        copyFrom(CopyOp(
            source = src,
            sourceX = 0,
            sourceY = 0,
            destX = 0,
            destY = 0,
            width = width,
            height = height
        ))
    }
}

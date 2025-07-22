package dev.deftu.omnicore.common

import net.minecraft.util.math.BlockPos

/**
 * Holds data about a chunk and provides block position utilities.
 */
public data class OmniChunkData(
    public val chunkX: Int,
    public val chunkZ: Int,
    public val blockStartX: Int,
    public val blockStartZ: Int,
    public val blockEndX: Int,
    public val blockEndZ: Int,
    private val posInvoker: (x: Int, y: Int, z: Int) -> BlockPos
) {

    init {
        require(this.blockEndX >= this.blockStartX) { "blockEndX must be greater than or equal to blockStartX" }
        require(this.blockEndZ >= this.blockStartZ) { "blockEndZ must be greater than or equal to blockStartZ" }
    }

    public fun getBlockPosOf(x: Int, y: Int, z: Int): BlockPos {
        return this.posInvoker(x, y, z)
    }

    public fun containsBlockAt(x: Int, z: Int): Boolean {
        return x in this.blockStartX..this.blockEndX && z in this.blockStartZ..this.blockEndZ
    }

}

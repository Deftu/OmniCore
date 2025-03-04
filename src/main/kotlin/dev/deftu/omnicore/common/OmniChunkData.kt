package dev.deftu.omnicore.common

import net.minecraft.util.math.BlockPos

public data class OmniChunkData(
    public val chunkX: Int,
    public val chunkZ: Int,
    public val blockStartX: Int,
    public val blockStartZ: Int,
    public val blockEndX: Int,
    public val blockEndZ: Int,
    private val posInvoker: (x: Int, y: Int, z: Int) -> BlockPos
) {

    public fun getBlockPosOf(x: Int, y: Int, z: Int): BlockPos {
        return posInvoker(x, y, z)
    }

}

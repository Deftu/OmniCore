@file:JvmName("OmniWorldSpace")

package dev.deftu.omnicore.api.data

public const val CHUNK_SIZE: Int = 16

public fun blockToChunkCoord(blockCoord: Int): Int {
    return blockCoord shr 4
}

public fun inChunkCoord(blockCoord: Int): Int {
    return blockCoord and (CHUNK_SIZE - 1)
}

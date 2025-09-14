@file:JvmName("OmniChunkPos")

package dev.deftu.omnicore.api.data

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import java.util.function.Consumer
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

public inline val ChunkPos.startBlock: BlockPos
    get() = BlockPos(this.startX, 0, this.startZ)

public inline val ChunkPos.middleX: Int
    get() = this.innerOffsetX(CHUNK_SIZE / 2)

public inline val ChunkPos.middleZ: Int
    get() = this.innerOffsetZ(CHUNK_SIZE / 2)

public inline val ChunkPos.centerBlock: BlockPos
    get() = BlockPos(this.middleX, 0, this.middleZ)

public inline val ChunkPos.endBlock: BlockPos
    get() = BlockPos(this.endX, 0, this.endZ)

public inline val ChunkPos.neighbors: Sequence<ChunkPos>
    get() = sequence {
        val cx = this@neighbors.x
        val cz = this@neighbors.z
        for (dz in -1..1) {
            for (dx in -1..1) {
                if (dx == 0 && dz == 0) continue
                yield(ChunkPos(cx + dx, cz + dz))
            }
        }
    }

@JvmName("create")
public fun chunkPos(x: Int, z: Int): ChunkPos {
    return ChunkPos(x, z)
}

@JvmName("create")
public fun chunkPos(x: Double, z: Double): ChunkPos {
    return ChunkPos(floor(x).toInt(), floor(z).toInt())
}

public fun ChunkPos.withX(x: Int): ChunkPos {
    return ChunkPos(x, this.z)
}

public fun ChunkPos.withZ(z: Int): ChunkPos {
    return ChunkPos(this.x, z)
}

public fun ChunkPos.innerOffsetX(x: Int): Int {
    return blockToChunkCoord(this.x) + x
}

public fun ChunkPos.innerOffsetZ(z: Int): Int {
    return blockToChunkCoord(this.z) + z
}

public fun ChunkPos.moved(dx: Int, dz: Int): ChunkPos {
    return ChunkPos(this.x + dx, this.z + dz)
}

public operator fun ChunkPos.plus(other: ChunkPos): ChunkPos {
    return ChunkPos(this.x + other.x, this.z + other.z)
}

public operator fun ChunkPos.minus(other: ChunkPos): ChunkPos {
    return ChunkPos(this.x - other.x, this.z - other.z)
}

public fun ChunkPos.manhattanDistance(other: ChunkPos): Int {
    return abs(this.x - other.x) + abs(this.z - other.z)
}

public fun ChunkPos.squaredDistanceTo(other: ChunkPos): Int {
    val dx = this.x - other.x
    val dz = this.z - other.z
    return dx * dx + dz * dz
}

@JvmOverloads
public fun ChunkPos.radius(
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    includeCenter: Boolean = true
): Sequence<ChunkPos> = sequence {
    require(radius >= 0) { "radius must be >= 0" }
    val cx = this@radius.x
    val cz = this@radius.z
    val r2 = radius * radius
    for (dz in -radius..radius) {
        for (dx in -radius..radius) {
            if (!metric.isInside2D(dx, dz, radius, r2)) continue
            if (!includeCenter && dx == 0 && dz == 0) continue
            yield(ChunkPos(cx + dx, cz + dz))
        }
    }
}

public fun ChunkPos.packIntoLong(): Long {
    return ChunkPos.toLong(this.x, this.z)
}

public fun Long.unpackToChunkPos(): ChunkPos {
    //#if MC >= 1.16.5
    return ChunkPos(this)
    //#else
    //$$ val x = this.toInt()
    //$$ val z = (this ushr 32).toInt()
    //$$ return ChunkPos(x, z)
    //#endif
}

public fun forEachChunk(min: ChunkPos, max: ChunkPos, action: Consumer<ChunkPos>) {
    val x0 = min(min.x, max.x)
    val z0 = min(min.z, max.z)
    val x1 = maxOf(min.x, max.x)
    val z1 = maxOf(min.z, max.z)
    for (x in x0..x1) {
        for (z in z0..z1) {
            action.accept(ChunkPos(x, z))
        }
    }
}

@JvmOverloads
public fun forEachChunkInRadius(
    center: ChunkPos,
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    includeCenter: Boolean = true,
    action: Consumer<ChunkPos>
) {
    require(radius >= 0) { "radius must be >= 0" }

    val cx = center.x
    val cz = center.z
    val r2 = radius * radius
    for (dz in -radius..radius) {
        for (dx in -radius..radius) {
            if (!metric.isInside2D(dx, dz, radius, r2)) continue
            if (!includeCenter && dx == 0 && dz == 0) continue
            action.accept(ChunkPos(cx + dx, cz + dz))
        }
    }
}

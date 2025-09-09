@file:JvmName("OmniBlockPos")
@file:Suppress("EnumValuesSoftDeprecate")

package dev.deftu.omnicore.api.data

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import java.util.function.Consumer
import kotlin.math.floor

public val BlockPos.neighbors: Sequence<BlockPos>
    get() {
        return sequence {
            for (direction in Direction.values()) {
                yield(offset(direction))
            }
        }
    }

@JvmName("create")
public fun blockPos(x: Int, y: Int, z: Int): BlockPos {
    return BlockPos(x, y, z)
}

@JvmName("create")
public fun blockPos(x: Double, y: Double, z: Double): BlockPos {
    return BlockPos(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())
}

public fun BlockPos.withX(x: Int): BlockPos {
    return BlockPos(x, this.y, this.z)
}

public fun BlockPos.withY(y: Int): BlockPos {
    return BlockPos(this.x, y, this.z)
}

public fun BlockPos.withZ(z: Int): BlockPos {
    return BlockPos(this.x, this.y, z)
}

public operator fun BlockPos.plus(other: Vec3i): BlockPos {
    return this.add(other)
}

public operator fun BlockPos.plus(other: Vec3d): BlockPos {
    return this.add(BlockPos(floor(other.x).toInt(), floor(other.y).toInt(), floor(other.z).toInt()))
}

public operator fun BlockPos.minus(other: Vec3i): BlockPos {
    return this.subtract(other)
}

public operator fun BlockPos.minus(other: Vec3d): BlockPos {
    return this.subtract(BlockPos(floor(other.x).toInt(), floor(other.y).toInt(), floor(other.z).toInt()))
}

@JvmOverloads
public fun BlockPos.moved(direction: Direction, distance: Int = 1): BlockPos {
    return this.offset(direction, distance)
}

public fun BlockPos.centered(): Vec3d {
    return Vec3d(this.x + 0.5, this.y + 0.5, this.z + 0.5)
}

@JvmOverloads
public fun BlockPos.radius2D(
    size: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    y: Int = this.y,
    isCenterIncluded: Boolean = true
): Sequence<BlockPos> {
    return sequence {
        require(size >= 0) { "size must be >= 0" }

        val radiusSq = size * size
        val (cx, cz) = intArrayOf(this@radius2D.x, this@radius2D.z)
        for (dz in -size..size) {
            for (dx in -size..size) {
                if (
                    !metric.isInside2D(dx, dz, size, radiusSq) ||
                    (!isCenterIncluded && dx == 0 && dz == 0 && y == this@radius2D.y)
                ) {
                    continue
                }

                yield(BlockPos(cx + dx, y, cz + dz))
            }
        }
    }
}

@JvmOverloads
public fun BlockPos.radius3D(
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    includeCenter: Boolean = true
): Sequence<BlockPos> {
    return sequence {
        require(radius >= 0) { "size must be >= 0" }

        val radiusSq = radius * radius
        val (cx, cy, cz) = intArrayOf(this@radius3D.x, this@radius3D.y, this@radius3D.z)
        for (dz in -radius..radius) {
            for (dy in -radius..radius) {
                for (dx in -radius..radius) {
                    if (
                        !metric.isInside3D(dx, dy, dz, radius, radiusSq) ||
                        (!includeCenter && dx == 0 && dy == 0 && dz == 0)
                    ) {
                        continue
                    }

                    yield(BlockPos(cx + dx, cy + dy, cz + dz))
                }
            }
        }
    }
}

public fun BlockPos.packIntoLong(): Long {
    //#if MC >= 1.16.5
    return BlockPos.asLong(this.x, this.y, this.z)
    //#else
    //$$ return this.toLong()
    //#endif
}

public fun Long.unpackToBlockPos(): BlockPos {
    return BlockPos.fromLong(this)
}

public fun forEachBlock(min: BlockPos, max: BlockPos, action: Consumer<BlockPos>) {
    val (x0, y0, z0) = intArrayOf(min.x, min.y, min.z)
    val (x1, y1, z1) = intArrayOf(max.x, max.y, max.z)
    for (x in minOf(x0, x1)..maxOf(x0, x1)) {
        for (y in minOf(y0, y1)..maxOf(y0, y1)) {
            for (z in minOf(z0, z1)..maxOf(z0, z1)) {
                action.accept(BlockPos(x, y, z))
            }
        }
    }
}

@JvmOverloads
public fun forEachBlockInRadius2D(
    center: BlockPos,
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    y: Int = center.y,
    includeCenter: Boolean = true,
    action: Consumer<BlockPos>
) {
    require(radius >= 0) { "size must be >= 0" }

    val radiusSq = radius * radius
    val (cx, cz) = intArrayOf(center.x, center.z)
    for (dz in -radius..radius) {
        for (dx in -radius..radius) {
            if (
                !metric.isInside2D(dx, dz, radius, radiusSq) ||
                (!includeCenter && dx == 0 && dz == 0 && y == center.y)
            ) {
                continue
            }

            action.accept(BlockPos(cx + dx, y, cz + dz))
        }
    }
}

@JvmOverloads
public fun forEachBlockInRadius3D(
    center: BlockPos,
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    includeCenter: Boolean = true,
    action: Consumer<BlockPos>
) {
    require(radius >= 0) { "size must be >= 0" }

    val radiusSq = radius * radius
    val (cx, cy, cz) = intArrayOf(center.x, center.y, center.z)
    for (dz in -radius..radius) {
        for (dy in -radius..radius) {
            for (dx in -radius..radius) {
                if (
                    !metric.isInside3D(dx, dy, dz, radius, radiusSq) ||
                    (!includeCenter && dx == 0 && dy == 0 && dz == 0)
                ) {
                    continue
                }

                action.accept(BlockPos(cx + dx, cy + dy, cz + dz))
            }
        }
    }
}

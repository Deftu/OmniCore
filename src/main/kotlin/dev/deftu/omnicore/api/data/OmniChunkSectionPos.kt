@file:JvmName("OmniChunkSectionPos")

package dev.deftu.omnicore.api.data

import net.minecraft.util.math.ChunkSectionPos
import java.util.function.Consumer
import kotlin.math.abs

public inline val ChunkSectionPos.neighbors: Sequence<ChunkSectionPos>
    get() = sequence {
        val sx = this@neighbors.x
        val sy = this@neighbors.y
        val sz = this@neighbors.z
        for (dz in -1..1) {
            for (dy in -1..1) {
                for (dx in -1..1) {
                    if (dx == 0 && dy == 0 && dz == 0) continue
                    yield(ChunkSectionPos.from(sx + dx, sy + dy, sz + dz))
                }
            }
        }
    }

public fun ChunkSectionPos.withX(x: Int): ChunkSectionPos {
    return ChunkSectionPos.from(x, this.y, this.z)
}

public fun ChunkSectionPos.withY(y: Int): ChunkSectionPos {
    return ChunkSectionPos.from(this.x, y, this.z)
}

public fun ChunkSectionPos.withZ(z: Int): ChunkSectionPos {
    return ChunkSectionPos.from(this.x, this.y, z)
}

public fun ChunkSectionPos.moved(dx: Int, dy: Int, dz: Int): ChunkSectionPos {
    return ChunkSectionPos.from(this.x + dx, this.y + dy, this.z + dz)
}

public operator fun ChunkSectionPos.plus(other: ChunkSectionPos): ChunkSectionPos {
    return ChunkSectionPos.from(this.x + other.x, this.y + other.y, this.z + other.z)
}

public operator fun ChunkSectionPos.minus(other: ChunkSectionPos): ChunkSectionPos {
    return ChunkSectionPos.from(this.x - other.x, this.y - other.y, this.z - other.z)
}

public fun ChunkSectionPos.manhattanDistance(other: ChunkSectionPos): Int {
    return abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z)
}

public fun ChunkSectionPos.squaredDistanceTo(other: ChunkSectionPos): Int {
    val dx = this.x - other.x
    val dy = this.y - other.y
    val dz = this.z - other.z
    return dx * dx + dy * dy + dz * dz
}

public fun ChunkSectionPos.packIntoLong(): Long {
    return ChunkSectionPos.asLong(this.x, this.y, this.z)
}

public fun Long.unpackToChunkSectionPos(): ChunkSectionPos {
    return ChunkSectionPos.from(this)
}

@JvmOverloads
public fun ChunkSectionPos.radius2D(
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    y: Int = this.y,
    includeCenter: Boolean = true
): Sequence<ChunkSectionPos> = sequence {
    require(radius >= 0) { "radius must be >= 0" }
    val cx = this@radius2D.x
    val cz = this@radius2D.z
    val r2 = radius * radius
    for (dz in -radius..radius) {
        for (dx in -radius..radius) {
            if (!metric.isInside2D(dx, dz, radius, r2)) continue
            if (!includeCenter && dx == 0 && dz == 0 && y == this@radius2D.y) continue
            yield(ChunkSectionPos.from(cx + dx, y, cz + dz))
        }
    }
}

@JvmOverloads
public fun ChunkSectionPos.radius3D(
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    includeCenter: Boolean = true
): Sequence<ChunkSectionPos> = sequence {
    require(radius >= 0) { "radius must be >= 0" }
    val cx = this@radius3D.x
    val cy = this@radius3D.y
    val cz = this@radius3D.z
    val r2 = radius * radius
    for (dz in -radius..radius) {
        for (dy in -radius..radius) {
            for (dx in -radius..radius) {
                if (!metric.isInside3D(dx, dy, dz, radius, r2)) continue
                if (!includeCenter && dx == 0 && dy == 0 && dz == 0) continue
                yield(ChunkSectionPos.from(cx + dx, cy + dy, cz + dz))
            }
        }
    }
}

public fun forEachSection(
    start: ChunkSectionPos,
    end: ChunkSectionPos,
    action: Consumer<ChunkSectionPos>
) {
    val x0 = minOf(start.x, end.x)
    val y0 = minOf(start.y, end.y)
    val z0 = minOf(start.z, end.z)
    val x1 = maxOf(start.x, end.x)
    val y1 = maxOf(start.y, end.y)
    val z1 = maxOf(start.z, end.z)
    for (x in x0..x1) {
        for (y in y0..y1) {
            for (z in z0..z1) {
                action.accept(ChunkSectionPos.from(x, y, z))
            }
        }
    }
}

@JvmOverloads
public fun forEachSectionInRadius2D(
    center: ChunkSectionPos,
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    y: Int = center.y,
    includeCenter: Boolean = true,
    action: Consumer<ChunkSectionPos>
) {
    require(radius >= 0) { "radius must be >= 0" }
    val cx = center.x
    val cz = center.z
    val r2 = radius * radius
    for (dz in -radius..radius) {
        for (dx in -radius..radius) {
            if (!metric.isInside2D(dx, dz, radius, r2)) continue
            if (!includeCenter && dx == 0 && dz == 0 && y == center.y) continue
            action.accept(ChunkSectionPos.from(cx + dx, y, cz + dz))
        }
    }
}

@JvmOverloads
public fun forEachSectionInRadius3D(
    center: ChunkSectionPos,
    radius: Int,
    metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
    includeCenter: Boolean = true,
    action: Consumer<ChunkSectionPos>
) {
    require(radius >= 0) { "radius must be >= 0" }
    val cx = center.x
    val cy = center.y
    val cz = center.z
    val r2 = radius * radius
    for (dz in -radius..radius) {
        for (dy in -radius..radius) {
            for (dx in -radius..radius) {
                if (!metric.isInside3D(dx, dy, dz, radius, r2)) continue
                if (!includeCenter && dx == 0 && dy == 0 && dz == 0) continue
                action.accept(ChunkSectionPos.from(cx + dx, cy + dy, cz + dz))
            }
        }
    }
}

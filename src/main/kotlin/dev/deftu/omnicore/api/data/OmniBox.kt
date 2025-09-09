@file:JvmName("OmniBox")

package dev.deftu.omnicore.api.data

import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

public val Box.min: Vec3d
    get() = Vec3d(minX, minY, minZ)

public val Box.max: Vec3d
    get() = Vec3d(maxX, maxY, maxZ)

public val Box.size: Vec3d
    get() = max - min

public val Box.center: Vec3d
    get() = Vec3d((minX + maxX) * 0.5, (minY + maxY) * 0.5, (minZ + maxZ) * 0.5)

@JvmName("create")
public fun aabb(min: Vec3d, max: Vec3d): Box {
    return Box(min.x, min.y, min.z, max.x, max.y, max.z)
}

@JvmName("create")
public fun aabb(center: Vec3d, halfX: Double, halfY: Double, halfZ: Double): Box {
    return Box(
        center.x - halfX, center.y - halfY, center.z - halfZ,
        center.x + halfX, center.y + halfY, center.z + halfZ
    )
}

@JvmName("create")
public fun aabb(vec3i: Vec3i): Box {
    return Box(vec3i.x.toDouble(), vec3i.y.toDouble(), vec3i.z.toDouble(), vec3i.x + 1.0, vec3i.y + 1.0, vec3i.z + 1.0)
}

public fun Box.withMinX(minX: Double): Box {
    return Box(minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ)
}

public fun Box.withMinY(minY: Double): Box {
    return Box(this.minX, minY, this.minZ, this.maxX, this.maxY, this.maxZ)
}

public fun Box.withMinZ(minZ: Double): Box {
    return Box(this.minX, this.minY, minZ, this.maxX, this.maxY, this.maxZ)
}

public fun Box.withMaxX(maxX: Double): Box {
    return Box(this.minX, this.minY, this.minZ, maxX, this.maxY, this.maxZ)
}

public fun Box.withMaxY(maxY: Double): Box {
    return Box(this.minX, this.minY, this.minZ, this.maxX, maxY, this.maxZ)
}

public fun Box.withMaxZ(maxZ: Double): Box {
    return Box(this.minX, this.minY, this.minZ, this.maxX, this.maxY, maxZ)
}

public fun Box.withMin(min: Vec3d): Box {
    return Box(min.x, min.y, min.z, this.maxX, this.maxY, this.maxZ)
}

public fun Box.withMax(max: Vec3d): Box {
    return Box(this.minX, this.minY, this.minZ, max.x, max.y, max.z)
}

public fun Box.expand(x: Double, y: Double, z: Double): Box {
    return Box(
        this.minX - x, this.minY - y, this.minZ - z,
        this.maxX + x, this.maxY + y, this.maxZ + z
    )
}

public fun Box.expand(vec: Vec3d): Box {
    return expand(vec.x, vec.y, vec.z)
}

public fun Box.expand(amount: Double): Box {
    return expand(amount, amount, amount)
}

public fun Box.contract(x: Double, y: Double, z: Double): Box {
    return Box(
        this.minX + x, this.minY + y, this.minZ + z,
        this.maxX - x, this.maxY - y, this.maxZ - z
    )
}

public fun Box.contract(vec: Vec3d): Box {
    return contract(vec.x, vec.y, vec.z)
}

public fun Box.contract(amount: Double): Box {
    return contract(amount, amount, amount)
}

public fun Box.translate(x: Double, y: Double, z: Double): Box {
    return Box(
        this.minX + x, this.minY + y, this.minZ + z,
        this.maxX + x, this.maxY + y, this.maxZ + z
    )
}

public fun Box.translate(vec: Vec3d): Box {
    return translate(vec.x, vec.y, vec.z)
}

public fun Box.translate(vec: Vec3i): Box {
    return translate(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
}

public fun Box.scale(factor: Double): Box {
    val cx = (minX + maxX) * 0.5
    val cy = (minY + maxY) * 0.5
    val cz = (minZ + maxZ) * 0.5
    val hx = (maxX - minX) * 0.5 * factor
    val hy = (maxY - minY) * 0.5 * factor
    val hz = (maxZ - minZ) * 0.5 * factor
    return Box(cx - hx, cy - hy, cz - hz, cx + hx, cy + hy, cz + hz)
}

public infix fun Box.union(other: Box): Box {
    return Box(
        minOf(this.minX, other.minX),
        minOf(this.minY, other.minY),
        minOf(this.minZ, other.minZ),
        maxOf(this.maxX, other.maxX),
        maxOf(this.maxY, other.maxY),
        maxOf(this.maxZ, other.maxZ)
    )
}

public infix fun Box.intersect(other: Box): Box? {
    val minX = maxOf(this.minX, other.minX)
    val minY = maxOf(this.minY, other.minY)
    val minZ = maxOf(this.minZ, other.minZ)
    val maxX = minOf(this.maxX, other.maxX)
    val maxY = minOf(this.maxY, other.maxY)
    val maxZ = minOf(this.maxZ, other.maxZ)
    return if (minX < maxX && minY < maxY && minZ < maxZ) {
        Box(minX, minY, minZ, maxX, maxY, maxZ)
    } else {
        null
    }
}

public fun Box.contains(box: Box): Boolean {
    return this.minX <= box.minX && this.minY <= box.minY && this.minZ <= box.minZ &&
            this.maxX >= box.maxX && this.maxY >= box.maxY && this.maxZ >= box.maxZ
}

public fun Box.contains(point: Vec3d): Boolean {
    return point.x in minX..maxX && point.y in minY..maxY && point.z in minZ..maxZ
}

public fun Box.contains(point: Vec3i): Boolean {
    return point.x.toDouble() in minX..maxX &&
            point.y.toDouble() in minY..maxY &&
            point.z.toDouble() in minZ..maxZ
}

public fun Box.contains(x: Double, y: Double, z: Double): Boolean {
    return x in minX..maxX && y in minY..maxY && z in minZ..maxZ
}

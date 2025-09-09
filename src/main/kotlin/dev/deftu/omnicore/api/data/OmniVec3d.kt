@file:JvmName("OmniVec3d")

package dev.deftu.omnicore.api.data

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import kotlin.math.abs
import kotlin.math.floor

@JvmName("create")
public fun vec3d(x: Double, y: Double, z: Double): Vec3d {
    return Vec3d(x, y, z)
}

@JvmName("create")
public fun vec3d(x: Int, y: Int, z: Int): Vec3d {
    return Vec3d(x.toDouble(), y.toDouble(), z.toDouble())
}

public fun Vec3d.withX(x: Double): Vec3d {
    return Vec3d(x, this.y, this.z)
}

public fun Vec3d.withY(y: Double): Vec3d {
    return Vec3d(this.x, y, this.z)
}

public fun Vec3d.withZ(z: Double): Vec3d {
    return Vec3d(this.x, this.y, z)
}

public operator fun Vec3d.plus(other: Vec3d): Vec3d {
    return this.add(other)
}

public operator fun Vec3d.plus(other: Vec3i): Vec3d {
    return this.add(vec3d(other.x, other.y, other.z))
}

public operator fun Vec3d.plus(other: Double): Vec3d {
    return this.add(other, other, other)
}

public operator fun Vec3d.minus(other: Vec3d): Vec3d {
    return this.subtract(other)
}

public operator fun Vec3d.minus(other: Vec3i): Vec3d {
    return this.subtract(vec3d(other.x, other.y, other.z))
}

public operator fun Vec3d.minus(other: Double): Vec3d {
    return this.subtract(other, other, other)
}

public operator fun Vec3d.times(other: Vec3d): Vec3d {
    return Vec3d(this.x * other.x, this.y * other.y, this.z * other.z)
}

public operator fun Vec3d.times(other: Vec3i): Vec3d {
    return Vec3d(this.x * other.x, this.y * other.y, this.z * other.z)
}

public operator fun Vec3d.times(other: Double): Vec3d {
    return Vec3d(this.x * other, this.y * other, this.z * other)
}

public operator fun Vec3d.div(other: Vec3d): Vec3d {
    return Vec3d(this.x / other.x, this.y / other.y, this.z / other.z)
}

public operator fun Vec3d.div(other: Vec3i): Vec3d {
    return Vec3d(this.x / other.x, this.y / other.y, this.z / other.z)
}

public operator fun Vec3d.div(other: Double): Vec3d {
    return Vec3d(this.x / other, this.y / other, this.z / other)
}

public fun Vec3d.floor(): Vec3i {
    return vec3i(floor(this.x).toInt(), floor(this.y).toInt(), floor(this.z).toInt())
}

public fun Vec3d.manhattanDistance(other: Vec3d): Double {
    return abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z)
}

public fun Vec3d.squaredDistance(other: Vec3d): Double {
    val dx = this.x - other.x
    val dy = this.y - other.y
    val dz = this.z - other.z
    return dx * dx + dy * dy + dz * dz
}

public fun Vec3d.toVec3i(): Vec3i {
    return vec3i(floor(this.x).toInt(), floor(this.y).toInt(), floor(this.z).toInt())
}

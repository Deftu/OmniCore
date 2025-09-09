@file:JvmName("OmniVec3i")

package dev.deftu.omnicore.api.data

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import kotlin.math.abs
import kotlin.math.floor

@JvmName("create")
public fun vec3i(x: Int, y: Int, z: Int): Vec3i {
    return Vec3i(x, y, z)
}

@JvmName("create")
public fun vec3i(x: Double, y: Double, z: Double): Vec3i {
    return Vec3i(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())
}

public fun Vec3i.withX(x: Int): Vec3i {
    return Vec3i(x, this.y, this.z)
}

public fun Vec3i.withY(y: Int): Vec3i {
    return Vec3i(this.x, y, this.z)
}

public fun Vec3i.withZ(z: Int): Vec3i {
    return Vec3i(this.x, this.y, z)
}

public operator fun Vec3i.plus(other: Vec3i): Vec3i {
    //#if MC >= 1.17.1
    return this.add(other)
    //#else
    //$$ return vec3i(this.x + other.x, this.y + other.y, this.z + other.z)
    //#endif
}

public operator fun Vec3i.plus(other: Vec3d): Vec3i {
    return this.plus(vec3i(other.x, other.y, other.z))
}

public operator fun Vec3i.plus(other: Int): Vec3i {
    //#if MC >= 1.17.1
    return this.add(other, other, other)
    //#else
    //$$ return vec3i(this.x + other, this.y + other, this.z + other)
    //#endif
}

public operator fun Vec3i.minus(other: Vec3i): Vec3i {
    //#if MC >= 1.17.1
    return this.subtract(other)
    //#else
    //$$ return vec3i(this.x - other.x, this.y - other.y, this.z - other.z)
    //#endif
}

public operator fun Vec3i.minus(other: Vec3d): Vec3i {
    return this.minus(vec3i(other.x, other.y, other.z))
}

public operator fun Vec3i.minus(other: Int): Vec3i {
    //#if MC >= 1.17.1
    return this.subtract(vec3i(other, other, other))
    //#else
    //$$ return vec3i(this.x - other, this.y - other, this.z - other)
    //#endif
}

public operator fun Vec3i.times(other: Vec3i): Vec3i {
    return Vec3i(this.x * other.x, this.y * other.y, this.z * other.z)
}

public operator fun Vec3i.times(other: Vec3d): Vec3i {
    return Vec3i(
        floor(this.x * other.x).toInt(),
        floor(this.y * other.y).toInt(),
        floor(this.z * other.z).toInt()
    )
}

public operator fun Vec3i.times(other: Int): Vec3i {
    return Vec3i(this.x * other, this.y * other, this.z * other)
}

public operator fun Vec3i.div(other: Vec3i): Vec3i {
    return Vec3i(this.x / other.x, this.y / other.y, this.z / other.z)
}

public operator fun Vec3i.div(other: Vec3d): Vec3i {
    return Vec3i(
        floor(this.x / other.x).toInt(),
        floor(this.y / other.y).toInt(),
        floor(this.z / other.z).toInt()
    )
}

public operator fun Vec3i.div(other: Int): Vec3i {
    return Vec3i(this.x / other, this.y / other, this.z / other)
}

public fun Vec3i.manhattanDistance(other: Vec3i): Int {
    return abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z)
}

public fun Vec3i.squaredDistanceTo(other: Vec3i): Double {
    val dx = (this.x - other.x).toDouble()
    val dy = (this.y - other.y).toDouble()
    val dz = (this.z - other.z).toDouble()
    return dx * dx + dy * dy + dz * dz
}

public fun Vec3i.toVec3d(): Vec3d {
    return Vec3d(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
}

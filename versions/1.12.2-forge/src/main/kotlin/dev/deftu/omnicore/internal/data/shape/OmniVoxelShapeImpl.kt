package dev.deftu.omnicore.internal.data.shape

import dev.deftu.omnicore.api.data.aabb.OmniAABB
import dev.deftu.omnicore.api.data.shape.OmniVoxelShape
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import dev.deftu.omnicore.api.direction.OmniDirectionalAxis
import java.util.function.Consumer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

public class OmniVoxelShapeImpl(override val boxes: List<OmniAABB>) : OmniVoxelShape {
    public companion object {
        private const val EPSILON = 1.0E-7

        @JvmField public val EMPTY: OmniVoxelShapeImpl = OmniVoxelShapeImpl(emptyList())

        @JvmStatic
        public fun cuboid(aabb: OmniAABB): OmniVoxelShape {
            return OmniVoxelShapeImpl(listOf(aabb))
        }

        @JvmStatic
        public fun fullCube(): OmniVoxelShape {
            return cuboid(OmniAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
        }

        private fun isAlmostEqual(a: Double, b: Double): Boolean {
            return abs(a - b) < EPSILON
        }
    }

    override val isEmpty: Boolean
        get() = boxes.isEmpty()

    override val isFullCube: Boolean
        get() {
            if (boxes.size != 1) {
                return false
            }

            val box = boxes[0]
            val isMinZero = isAlmostEqual(box.minX, 0.0) &&
                    isAlmostEqual(box.minY, 0.0) &&
                    isAlmostEqual(box.minZ, 0.0)
            val isMaxOne = isAlmostEqual(box.maxX, 1.0) &&
                    isAlmostEqual(box.maxY, 1.0) &&
                    isAlmostEqual(box.maxZ, 1.0)
            return isMinZero && isMaxOne
        }

    override val size: Int
        get() = boxes.size

    override val edges: List<OmniAABB>
        get() {
            return buildList {
                for (box in boxes) {
                    // 12 edges of the box
                    add(OmniAABB(box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ)) // Edge 1
                    add(OmniAABB(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ)) // Edge 2
                    add(OmniAABB(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ)) // Edge 3
                    add(OmniAABB(box.maxX, box.maxY, box.maxZ, box.maxX, box.maxY, box.minZ)) // Edge 4
                    add(OmniAABB(box.maxX, box.maxY, box.maxZ, box.maxX, box.minY, box.maxZ)) // Edge 5
                    add(OmniAABB(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ)) // Edge 6
                    add(OmniAABB(box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ)) // Edge 7
                    add(OmniAABB(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ)) // Edge 8
                    add(OmniAABB(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ)) // Edge 9
                    add(OmniAABB(box.maxX, box.minY, box.minZ, box.minX, box.minY, box.minZ)) // Edge 10
                    add(OmniAABB(box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ)) // Edge 11
                    add(OmniAABB(box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ)) // Edge 12
                }
            }
        }

    override fun forEachBox(consumer: Consumer<OmniAABB>) {
        for (box in boxes) {
            consumer.accept(box)
        }
    }

    override fun forEachEdge(consumer: Consumer<OmniAABB>) {
        for (edge in edges) {
            consumer.accept(edge)
        }
    }

    override fun offset(
        x: Double,
        y: Double,
        z: Double
    ): OmniVoxelShape {
        if (boxes.isEmpty()) {
            return this
        }

        return OmniVoxelShapeImpl(List(size) {
            val box = boxes[it]
            box.offset(x, y, z)
        })
    }

    override fun union(other: OmniVoxelShape): OmniVoxelShape {
        if (other.isEmpty) {
            return this
        }

        if (isEmpty) {
            return other
        }

        return OmniVoxelShapeImpl(boxes + other.boxes)
    }

    override fun union(box: OmniAABB): OmniVoxelShape {
        if (isEmpty) {
            return cuboid(box)
        }

        return OmniVoxelShapeImpl(boxes + box)
    }

    override fun intersect(other: OmniVoxelShape): OmniVoxelShape {
        if (isEmpty || other.isEmpty) {
            return EMPTY
        }

        return OmniVoxelShapeImpl(buildList {
            for (boxA in boxes) {
                for (boxB in other.boxes) {
                    add(boxA.intersection(boxB))
                }
            }
        })
    }

    override fun intersect(box: OmniAABB): OmniVoxelShape {
        if (isEmpty) {
            return EMPTY
        }

        return OmniVoxelShapeImpl(buildList {
            for (boxA in boxes) {
                add(boxA.intersection(box))
            }
        })
    }

    override fun subtract(other: OmniVoxelShape): OmniVoxelShape {
        if (isEmpty) {
            return EMPTY
        }

        if (other.isEmpty) {
            return this
        }

        var current = this.boxes
        for (cut in other.boxes) {
            current = subtractBoxList(current, cut)
            if (current.isEmpty()) {
                break
            }
        }

        return OmniVoxelShapeImpl(current)
    }

    override fun subtract(box: OmniAABB): OmniVoxelShape {
        if (isEmpty) {
            return EMPTY
        }

        return OmniVoxelShapeImpl(subtractBoxList(boxes, box))
    }

    override fun simplify(): OmniVoxelShape {
        if (boxes.size <= 1) {
            return this
        }

        var list = coalesceAxis(boxes, OmniDirectionalAxis.X)
        list = coalesceAxis(list, OmniDirectionalAxis.Y)
        list = coalesceAxis(list, OmniDirectionalAxis.Z)
        return OmniVoxelShapeImpl(list)
    }

    override fun contains(x: Double, y: Double, z: Double): Boolean {
        if (boxes.isEmpty()) {
            return false
        }

        for (b in boxes) {
            val insideX = x >= b.minX && x <= b.maxX
            if (!insideX) {
                continue
            }

            val insideY = y >= b.minY && y <= b.maxY
            if (!insideY) {
                continue
            }

            val insideZ = z >= b.minZ && z <= b.maxZ
            if (!insideZ) {
                continue
            }

            return true
        }

        return false
    }

    override fun intersects(box: OmniAABB): Boolean {
        if (boxes.isEmpty()) {
            return false
        }

        for (a in boxes) {
            if (a.maxX <= box.minX || a.minX >= box.maxX) {
                continue
            }

            if (a.maxY <= box.minY || a.minY >= box.maxY) {
                continue
            }

            if (a.maxZ <= box.minZ || a.minZ >= box.maxZ) {
                continue
            }

            return true
        }

        return false
    }

    override fun raycast(
        from: OmniVec3d,
        to: OmniVec3d
    ): Boolean {
        if (boxes.isEmpty()) {
            return false
        }

        val ox = from.x
        val oy = from.y
        val oz = from.z
        val dx = to.x - from.x
        val dy = to.y - from.y
        val dz = to.z - from.z

        for (box in boxes) {
            if (checkRayIntersectionWithSegment(
                    ox, oy, oz,
                    dx, dy, dz,
                    box.minX, box.minY, box.minZ,
                    box.maxX, box.maxY, box.maxZ
                )
            ) {
                return true
            }
        }

        return false
    }

    private fun intersectAABB(first: OmniAABB, second: OmniAABB): OmniAABB? {
        val minX = max(first.minX, second.minX)
        val minY = max(first.minY, second.minY)
        val minZ = max(first.minZ, second.minZ)
        val maxX = min(first.maxX, second.maxX)
        val maxY = min(first.maxY, second.maxY)
        val maxZ = min(first.maxZ, second.maxZ)
        if (minX >= maxX || minY >= maxY || minZ >= maxZ) {
            return null
        }

        return OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    private fun subtractBoxList(input: List<OmniAABB>, cut: OmniAABB): List<OmniAABB> {
        if (input.isEmpty()) {
            return emptyList()
        }

        return buildList(input.size + 6) {
            for (src in input) {
                val intersection = intersectAABB(src, cut)
                if (intersection == null) {
                    add(src)
                    continue
                }

                // Left slab (X-)
                if (src.minX < intersection.minX) {
                    add(OmniAABB(src.minX, src.minY, src.minZ, intersection.minX, src.maxY, src.maxZ))
                }

                // Right slab (X+)
                if (src.maxX > intersection.maxX) {
                    add(OmniAABB(intersection.maxX, src.minY, src.minZ, src.maxX, src.maxY, src.maxZ))
                }

                // Middle region (X)
                val midMinX = max(src.minX, intersection.minX)
                val midMaxX = min(src.maxX, intersection.maxX)

                // Bottom slab (Y-)
                if (src.minY < intersection.minY) {
                    add(OmniAABB(midMinX, src.minY, src.minZ, midMaxX, intersection.minY, src.maxZ))
                }

                // Top slab (Y+)
                if (src.maxY > intersection.maxY) {
                    add(OmniAABB(midMinX, intersection.maxY, src.minZ, midMaxX, src.maxY, src.maxZ))
                }

                // Middle region (Y)
                val midMinY = max(src.minY, intersection.minY)
                val midMaxY = min(src.maxY, intersection.maxY)

                // Front slab (Z-)
                if (src.minZ < intersection.minZ) {
                    add(OmniAABB(midMinX, midMinY, src.minZ, midMaxX, midMaxY, intersection.minZ))
                }

                // Back slab (Z+)
                if (src.maxZ > intersection.maxZ) {
                    add(OmniAABB(midMinX, midMinY, intersection.maxZ, midMaxX, midMaxY, src.maxZ))
                }
            }
        }
    }

    private fun coalesceAxis(input: List<OmniAABB>, axis: OmniDirectionalAxis): List<OmniAABB> {
        if (input.size <= 1) {
            return input
        }

        val output = input.toMutableList()
        var i = 0
        while (i < output.size) {
            var j = i + 1
            var didMerge = false
            while (j < output.size) {
                val first = output[i]
                val second = output[j]
                val merged = tryMergeAlongAxis(first, second, axis)
                if (merged != null) {
                    output[i] = merged
                    output.removeAt(j)
                    didMerge = true
                } else {
                    j++
                }
            }

            if (!didMerge) {
                i++
            }
        }

        return output.toList()
    }

    private fun tryMergeAlongAxis(a: OmniAABB, b: OmniAABB, axis: OmniDirectionalAxis): OmniAABB? {
        return when (axis) {
            OmniDirectionalAxis.X -> {
                val sameY = isAlmostEqual(a.minY, b.minY) && isAlmostEqual(a.maxY, b.maxY)
                val sameZ = isAlmostEqual(a.minZ, b.minZ) && isAlmostEqual(a.maxZ, b.maxZ)
                if (!sameY || !sameZ) {
                    null
                } else {
                    val touches = isAlmostEqual(a.maxX, b.minX) || isAlmostEqual(b.maxX, a.minX)
                    val overlaps = a.minX <= b.maxX && b.minX <= a.maxX
                    if (!touches && !overlaps) {
                        null
                    } else {
                        OmniAABB(min(a.minX, b.minX), a.minY, a.minZ, max(a.maxX, b.maxX), a.maxY, a.maxZ)
                    }
                }
            }
            OmniDirectionalAxis.Y -> {
                val sameX = isAlmostEqual(a.minX, b.minX) && isAlmostEqual(a.maxX, b.maxX)
                val sameZ = isAlmostEqual(a.minZ, b.minZ) && isAlmostEqual(a.maxZ, b.maxZ)
                if (!sameX || !sameZ) {
                    null
                } else {
                    val touches = isAlmostEqual(a.maxY, b.minY) || isAlmostEqual(b.maxY, a.minY)
                    val overlaps = a.minY <= b.maxY && b.minY <= a.maxY
                    if (!touches && !overlaps) {
                        null
                    } else {
                        OmniAABB(a.minX, min(a.minY, b.minY), a.minZ, a.maxX, max(a.maxY, b.maxY), a.maxZ)
                    }
                }
            }
            OmniDirectionalAxis.Z -> {
                val sameX = isAlmostEqual(a.minX, b.minX) && isAlmostEqual(a.maxX, b.maxX)
                val sameY = isAlmostEqual(a.minY, b.minY) && isAlmostEqual(a.maxY, b.maxY)
                if (!sameX || !sameY) {
                    null
                } else {
                    val touches = isAlmostEqual(a.maxZ, b.minZ) || isAlmostEqual(b.maxZ, a.minZ)
                    val overlaps = a.minZ <= b.maxZ && b.minZ <= a.maxZ
                    if (!touches && !overlaps) {
                        null
                    } else {
                        OmniAABB(a.minX, a.minY, min(a.minZ, b.minZ), a.maxX, a.maxY, max(a.maxZ, b.maxZ))
                    }
                }
            }
        }
    }

    private fun checkRayIntersectionWithSegment(
        ox: Double, oy: Double, oz: Double,
        dx: Double, dy: Double, dz: Double,
        minX: Double, minY: Double, minZ: Double,
        maxX: Double, maxY: Double, maxZ: Double
    ): Boolean {
        var tMin = 0.0
        var tMax = 1.0

        if (!intersectSlab(ox, dx, minX, maxX).also { (t0, t1) ->
                tMin = max(tMin, t0)
                tMax = min(tMax, t1)
            }.let { tMax >= tMin }) {
            return false
        }

        if (!intersectSlab(oy, dy, minY, maxY).also { (t0, t1) ->
                tMin = max(tMin, t0)
                tMax = min(tMax, t1)
            }.let { tMax >= tMin }) {
            return false
        }
        if (!intersectSlab(oz, dz, minZ, maxZ).also { (t0, t1) ->
                tMin = max(tMin, t0)
                tMax = min(tMax, t1)
            }.let { tMax >= tMin }) {
            return false
        }

        if (tMax < 0.0) {
            return false
        }

        return true
    }

    private fun intersectSlab(
        o: Double,
        d: Double,
        mn: Double,
        mx: Double,
    ): Pair<Double, Double> {
        if (abs(d) <= 1.0E-12) {
            return if (o !in mn..mx) {
                // Parallel and outside slab -> no intersection; t range empty.
                Pair(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)
            } else {
                // Parallel and inside slab -> no constraint on t for this axis.
                Pair(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
            }
        } else {
            val inv = 1.0 / d
            var t0 = (mn - o) * inv
            var t1 = (mx - o) * inv
            if (t0 > t1) {
                val tmp = t0
                t0 = t1
                t1 = tmp
            }

            return Pair(t0, t1)
        }
    }
}

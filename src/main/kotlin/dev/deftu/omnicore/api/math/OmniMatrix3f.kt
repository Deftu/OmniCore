package dev.deftu.omnicore.api.math

import org.joml.Matrix3f
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

//#if MC <= 1.19.2
//$$ import org.lwjgl.BufferUtils
//#endif

//#if MC == 1.16.5
//$$ import dev.deftu.omnicore.internal.mixins.Mixin_Matrix3fGetters
//#endif

/**
 * 3x3 matrix for linear transforms (e.g., rotation, scaling, normals) in 3D space.
 *
 * Internally uses a column-major [FloatArray] of size 9 to store matrix elements.
 */
public class OmniMatrix3f private constructor(private val data: FloatArray) {
    public companion object {
        @JvmStatic
        public fun identity(): OmniMatrix3f {
            return OmniMatrix3f(floatArrayOf(
                1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f
            ))
        }

        @JvmStatic
        public fun from(data: FloatArray): OmniMatrix3f {
            return OmniMatrix3f(data.copyOf())
        }

        @JvmStatic
        public fun from(matrix: Matrix3f): OmniMatrix3f {
            val output = FloatArray(9)
            //#if MC >= 1.19.4
            matrix.get(output)
            //#elseif MC == 1.16.5
            //$$ val accessor = matrix as Mixin_Matrix3fGetters
            //$$ output[0] = accessor.getM00(); output[3] = accessor.getM01(); output[6] = accessor.getM02()
            //$$ output[1] = accessor.getM10(); output[4] = accessor.getM11(); output[7] = accessor.getM12()
            //$$ output[2] = accessor.getM20(); output[5] = accessor.getM21(); output[8] = accessor.getM22()
            //#else
            //$$ val buffer = BufferUtils.createFloatBuffer(9)
            //#if MC >= 1.17.1
            //$$ matrix.store(buffer)
            //$$ for (i in output.indices) {
            //$$     output[i] = buffer.get(i)
            //$$ }
            //#else
            //$$ matrix.store(buffer)
            //$$ buffer.flip()
            //$$ buffer.get(output)
            //#endif
            //#endif
            return OmniMatrix3f(output)
        }

        @JvmStatic
        public fun fromRowMajor(data: FloatArray): OmniMatrix3f {
            require(data.size == 9) { "Input array must have exactly 9 elements, but has ${data.size}" }
            return OmniMatrix3f(FloatArray(9).apply {
                for (row in 0 until 3) {
                    for (col in 0 until 3) {
                        this[col * 3 + row] = data[row * 3 + col]
                    }
                }
            })
        }

        @JvmStatic
        public fun scaled(x: Float, y: Float, z: Float): OmniMatrix3f {
            return OmniMatrix3f(floatArrayOf(
                x, 0f, 0f,
                0f, y, 0f,
                0f, 0f, z
            ))
        }

        @JvmStatic
        public fun rotationX(angle: Float): OmniMatrix3f {
            val c = cos(angle)
            val s = sin(angle)
            return OmniMatrix3f(floatArrayOf(
                1f, 0f, 0f,
                0f, c, s,
                0f, -s, c
            ))
        }

        @JvmStatic
        public fun rotationY(angle: Float): OmniMatrix3f {
            val c = cos(angle)
            val s = sin(angle)
            return OmniMatrix3f(floatArrayOf(
                c, 0f, -s,
                0f, 1f, 0f,
                s, 0f, c
            ))
        }

        @JvmStatic
        public fun rotationZ(angle: Float): OmniMatrix3f {
            val c = cos(angle)
            val s = sin(angle)
            return OmniMatrix3f(floatArrayOf(
                c, s, 0f,
                -s, c, 0f,
                0f, 0f, 1f
            ))
        }

        @JvmStatic
        public fun rotation(axisX: Float, axisY: Float, axisZ: Float, angle: Float): OmniMatrix3f {
            var nx = axisX
            var ny = axisY
            var nz = axisZ
            val axisLen = sqrt(nx * nx + ny * ny + nz * nz)
            require(axisLen > 0f) { "Axis length must be > 0" }
            nx /= axisLen; ny /= axisLen; nz /= axisLen

            val c = cos(angle)
            val s = sin(angle)
            val t = 1f - c
            return OmniMatrix3f(
                floatArrayOf(
                    t * nx * nx + c, t * nx * ny + s * nz, t * nx * nz - s * ny,
                    t * nx * ny - s * nz, t * ny * ny + c, t * ny * nz + s * nx,
                    t * nx * nz + s * ny, t * ny * nz - s * nx, t * nz * nz + c
                )
            )
        }
    }

    public class Builder @JvmOverloads public constructor(item: OmniMatrix3f? = null) {
        private val data = FloatArray(9) { if (it % 4 == 0) 1f else 0f } // Identity

        init {
            if (item != null) {
                for (i in data.indices) {
                    data[i] = item.data[i]
                }
            }
        }

        public fun set(row: Int, column: Int, value: Float): Builder {
            require(row in 0..2 && column in 0..2) { "Index out of bounds: ($row, $column)" }
            data[column * 3 + row] = value
            return this
        }

        public fun m00(value: Float): Builder = set(0, 0, value)
        public fun m01(value: Float): Builder = set(0, 1, value)
        public fun m02(value: Float): Builder = set(0, 2, value)
        public fun m10(value: Float): Builder = set(1, 0, value)
        public fun m11(value: Float): Builder = set(1, 1, value)
        public fun m12(value: Float): Builder = set(1, 2, value)
        public fun m20(value: Float): Builder = set(2, 0, value)
        public fun m21(value: Float): Builder = set(2, 1, value)
        public fun m22(value: Float): Builder = set(2, 2, value)

        public fun build(): OmniMatrix3f {
            return OmniMatrix3f(data)
        }
    }

    public val m00: Float get() = data[0]
    public val m01: Float get() = data[3]
    public val m02: Float get() = data[6]
    public val m10: Float get() = data[1]
    public val m11: Float get() = data[4]
    public val m12: Float get() = data[7]
    public val m20: Float get() = data[2]
    public val m21: Float get() = data[5]
    public val m22: Float get() = data[8]

    public val vanilla: Matrix3f
        get() = applyTo(Matrix3f())

    init {
        require(data.size == 9) { "Matrix must have exactly 9 elements, but has ${data.size}" }
    }

    public operator fun get(row: Int, column: Int): Float {
        require(row in 0..2 && column in 0..2) { "Index out of bounds: ($row, $column)" }
        return data[column * 3 + row]
    }

    public fun multiply(other: OmniMatrix3f): OmniMatrix3f {
        val left = this.data
        val right = other.data
        val result = FloatArray(9)

        for (column in 0..2) {
            val r0 = right[column * 3 + 0]
            val r1 = right[column * 3 + 1]
            val r2 = right[column * 3 + 2]

            result[column * 3 + 0] = left[0] * r0 + left[3] * r1 + left[6] * r2
            result[column * 3 + 1] = left[1] * r0 + left[4] * r1 + left[7] * r2
            result[column * 3 + 2] = left[2] * r0 + left[5] * r1 + left[8] * r2
        }

        return OmniMatrix3f(result)
    }

    public fun scale(x: Float, y: Float, z: Float): OmniMatrix3f {
        data[0] *= x; data[1] *= x; data[2] *= x
        data[3] *= y; data[4] *= y; data[5] *= y
        data[6] *= z; data[7] *= z; data[8] *= z

        return this
    }

    public fun scale(factor: Float): OmniMatrix3f {
        return scale(factor, factor, factor)
    }

    public fun rotateX(angle: Float): OmniMatrix3f {
        val c = cos(angle)
        val s = sin(angle)

        val col1r0 = data[3]; val col1r1 = data[4]; val col1r2 = data[5]
        val col2r0 = data[6]; val col2r1 = data[7]; val col2r2 = data[8]

        data[3] = c * col1r0 + s * col2r0
        data[4] = c * col1r1 + s * col2r1
        data[5] = c * col1r2 + s * col2r2
        data[6] = -s * col1r0 + c * col2r0
        data[7] = -s * col1r1 + c * col2r1
        data[8] = -s * col1r2 + c * col2r2
        return this
    }

    public fun rotateY(angle: Float): OmniMatrix3f {
        val c = cos(angle)
        val s = sin(angle)

        val col0r0 = data[0]; val col0r1 = data[1]; val col0r2 = data[2]
        val col2r0 = data[6]; val col2r1 = data[7]; val col2r2 = data[8]

        // new col0 =  c*old col0 + s*old col2
        data[0] = c * col0r0 + s * col2r0
        data[1] = c * col0r1 + s * col2r1
        data[2] = c * col0r2 + s * col2r2
        // new col2 = -s*old col0 + c*old col2
        data[6] = -s * col0r0 + c * col2r0
        data[7] = -s * col0r1 + c * col2r1
        data[8] = -s * col0r2 + c * col2r2
        return this
    }

    public fun rotateZ(angle: Float): OmniMatrix3f {
        val c = cos(angle)
        val s = sin(angle)

        val col0r0 = data[0]; val col0r1 = data[1]; val col0r2 = data[2]
        val col1r0 = data[3]; val col1r1 = data[4]; val col1r2 = data[5]

        // new col0 =  c*old col0 + s*old col1
        data[0] = c * col0r0 + s * col1r0
        data[1] = c * col0r1 + s * col1r1
        data[2] = c * col0r2 + s * col1r2
        // new col1 = -s*old col0 + c*old col1
        data[3] = -s * col0r0 + c * col1r0
        data[4] = -s * col0r1 + c * col1r1
        data[5] = -s * col0r2 + c * col1r2
        return this
    }

    public fun rotate(axisX: Float, axisY: Float, axisZ: Float, angle: Float): OmniMatrix3f {
        val rotationMatrix = rotation(axisX, axisY, axisZ, angle)
        postMultiplyInPlace(rotationMatrix.data)
        return this
    }

    public fun transpose(): OmniMatrix3f {
        val transposed = FloatArray(9)
        for (row in 0..2) for (column in 0..2) {
            transposed[column * 3 + row] = data[row * 3 + column]
        }

        return OmniMatrix3f(transposed)
    }

    public fun transformDirection(x: Float, y: Float, z: Float): OmniVector3f {
        val xPrime = data[0] * x + data[3] * y + data[6] * z
        val yPrime = data[1] * x + data[4] * y + data[7] * z
        val zPrime = data[2] * x + data[5] * y + data[8] * z
        return OmniVector3f(xPrime, yPrime, zPrime)
    }

    public fun deepCopy(): OmniMatrix3f {
        return OmniMatrix3f(data.copyOf())
    }

    public fun toArray(): FloatArray {
        return data.copyOf()
    }

    public fun toRowMajor(): FloatArray {
        val out = FloatArray(9)
        for (row in 0..2) for (column in 0..2) {
            out[row * 3 + column] = data[column * 3 + row]
        }

        return out
    }

    public fun applyTo(dest: Matrix3f): Matrix3f {
        //#if MC >= 1.19.4
        dest.m00(data[0]); dest.m01(data[3]); dest.m02(data[6])
        dest.m10(data[1]); dest.m11(data[4]); dest.m12(data[7])
        dest.m20(data[2]); dest.m21(data[5]); dest.m22(data[8])
        //#elseif MC >= 1.16.5
        //$$ for (row in 0..2) {
        //$$     for (col in 0..2) {
        //$$         dest[row, col] = this[row, col]
        //$$     }
        //$$ }
        //#else
        //$$ dest.m00 = data[0]; dest.m01 = data[3]; dest.m02 = data[6]
        //$$ dest.m10 = data[1]; dest.m11 = data[4]; dest.m12 = data[7]
        //$$ dest.m20 = data[2]; dest.m21 = data[5]; dest.m22 = data[8]
        //#endif
        return dest
    }

    public fun applyFrom(other: OmniMatrix3f): OmniMatrix3f {
        for (i in data.indices) {
            data[i] = other.data[i]
        }

        return this
    }

    public fun toBuilder(): Builder {
        return Builder(this)
    }

    override fun toString(): String {
        return buildString {
            append("OmniMatrix3f(\n")
            for (row in 0..2) {
                append("  ")
                for (column in 0..2) {
                    append(String.format("% .4f", this@OmniMatrix3f[row, column]))
                    if (column < 2) append(", ")
                }

                append("\n")
            }

            append(")")
        }
    }

    private fun postMultiplyInPlace(rhs: FloatArray) {
        val left = this.data
        val right = rhs
        val result = FloatArray(9)

        for (column in 0..2) {
            val r0 = right[column * 3 + 0]
            val r1 = right[column * 3 + 1]
            val r2 = right[column * 3 + 2]

            result[column * 3 + 0] = left[0] * r0 + left[3] * r1 + left[6] * r2
            result[column * 3 + 1] = left[1] * r0 + left[4] * r1 + left[7] * r2
            result[column * 3 + 2] = left[2] * r0 + left[5] * r1 + left[8] * r2
        }

        for (i in 0 until 9) {
            data[i] = result[i]
        }
    }
}

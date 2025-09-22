package dev.deftu.omnicore.api.math

import org.joml.Matrix4f
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

//#if MC <= 1.19.2
//$$ import org.lwjgl.BufferUtils
//#endif

//#if MC == 1.16.5
//$$ import dev.deftu.omnicore.internal.mixins.Mixin_Matrix4fSetters
//#endif

/**
 * 4x4 matrix for graphics and transformations.
 *
 * Internally uses a column-major [FloatArray] of size 16 to store matrix elements.
 * Safe for use with `glUniformMatrix4fv(..., transpose = false, ...)`.
 */
public class OmniMatrix4f private constructor(private val data: FloatArray) {
    public companion object {
        @JvmStatic
        public fun identity(): OmniMatrix4f {
            return OmniMatrix4f(floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            ))
        }

        @JvmStatic
        public fun from(data: FloatArray): OmniMatrix4f {
            return OmniMatrix4f(data.copyOf())
        }

        /** Build from a **row-major** array by transposing into our column-major storage. */
        @JvmStatic
        public fun fromRowMajor(data: FloatArray): OmniMatrix4f {
            require(data.size == 16) { "Matrix must have exactly 16 elements, but has ${data.size}" }
            return OmniMatrix4f(FloatArray(16).apply {
                for (row in 0..3) {
                    for (col in 0..3) {
                        this[col * 4 + row] = data[row * 4 + col]
                    }
                }
            })
        }

        @JvmStatic
        public fun from(matrix: Matrix4f): OmniMatrix4f {
            val output = FloatArray(16)
            //#if MC >= 1.19.4
            matrix.get(output)
            //#else
            //$$ val buffer = BufferUtils.createFloatBuffer(16)
            //$$ matrix.store(buffer)
            //#if MC >= 1.17.1
            //$$ for (i in output.indices) {
            //$$     output[i] = buffer.get(i)
            //$$ }
            //#else
            //$$ buffer.flip()
            //$$ buffer.get(output)
            //#endif
            //#endif
            return OmniMatrix4f(output)
        }

        /**
         * Right-handed perspective projection matrix.
         *
         * @param fovY Vertical field of view in radians.
         * @param aspect Aspect ratio (width / height).
         * @param zNear Near clipping plane distance (must be > 0).
         * @param zFar Far clipping plane distance (must be > zNear).
         * @return Perspective projection matrix.
         * @throws IllegalArgumentException if zNear <= 0 or zFar <= zNear
         */
        @JvmStatic
        public fun perspective(fovY: Float, aspect: Float, zNear: Float, zFar: Float): OmniMatrix4f {
            require(zNear > 0f) { "zNear must be greater than 0, but is $zNear" }
            require(zFar > zNear) { "zFar must be greater than zNear, but is $zFar <= $zNear" }

            val focal = 1f / tan(fovY * 0.5f)
            val nearFarInv = 1f / (zNear - zFar)
            val data = floatArrayOf(
                focal / aspect, 0f, 0f, 0f,
                0f, focal, 0f, 0f,
                0f, 0f, (zFar + zNear) * nearFarInv, -1f,
                0f, 0f, (2f * zFar * zNear) * nearFarInv, 0f
            )

            return OmniMatrix4f(data)
        }

        /**
         * Right-handed orthographic projection.
         */
        @JvmStatic
        public fun ortho(left: Float, right: Float, bottom: Float, top: Float, zNear: Float, zFar: Float): OmniMatrix4f {
            val invWidth = 1f / (right - left)
            val invHeight = 1f / (top - bottom)
            val invDepth = 1f / (zFar - zNear)
            val data = floatArrayOf(
                2f * invWidth, 0f, 0f, 0f,
                0f, 2f * invHeight, 0f, 0f,
                0f, 0f, -2f * invDepth, 0f,
                -(right + left) * invWidth,
                -(top + bottom) * invHeight,
                -(zFar + zNear) * invDepth,
                1f
            )

            return OmniMatrix4f(data)
        }

        /**
         * Right-handed look-at matrix.
         */
        @JvmStatic
        public fun lookAt(
            eyeX: Float, eyeY: Float, eyeZ: Float,
            centerX: Float, centerY: Float, centerZ: Float,
            upX: Float, upY: Float, upZ: Float
        ): OmniMatrix4f {
            // forward = normalize(center - eye)
            var fx = centerX - eyeX
            var fy = centerY - eyeY
            var fz = centerZ - eyeZ
            val forwardLenInv = 1f / sqrt(fx * fx + fy * fy + fz * fz)
            fx *= forwardLenInv; fy *= forwardLenInv; fz *= forwardLenInv

            // side = normalize(forward × up)
            var sx = fy * upZ - fz * upY
            var sy = fz * upX - fx * upZ
            var sz = fx * upY - fy * upX
            val sideLenInv = 1f / sqrt(sx * sx + sy * sy + sz * sz)
            sx *= sideLenInv; sy *= sideLenInv; sz *= sideLenInv

            // up = side × forward
            val upPx = sy * fz - sz * fy
            val upPy = sz * fx - sx * fz
            val upPz = sx * fy - sy * fx

            val rotation = floatArrayOf(
                sx, upPx, -fx, 0f,
                sy, upPy, -fy, 0f,
                sz, upPz, -fz, 0f,
                0f, 0f, 0f, 1f
            )

            val translation = translation(-eyeX, -eyeY, -eyeZ)
            return OmniMatrix4f(rotation).multiply(translation)
        }

        public fun translation(x: Float, y: Float, z: Float): OmniMatrix4f {
            val data = identity().data
            data[12] = x
            data[13] = y
            data[14] = z
            return OmniMatrix4f(data)
        }

        @JvmStatic
        public fun scaled(x: Float, y: Float, z: Float): OmniMatrix4f {
            val matrix = floatArrayOf(
                x, 0f, 0f, 0f,
                0f, y, 0f, 0f,
                0f, 0f, z, 0f,
                0f, 0f, 0f, 1f
            )

            return OmniMatrix4f(matrix)
        }

        @JvmStatic
        public fun rotationX(angle: Float): OmniMatrix4f {
            val c = cos(angle); val s = sin(angle)
            return OmniMatrix4f(
                floatArrayOf(
                    1f, 0f, 0f, 0f,
                    0f, c, s, 0f,
                    0f, -s, c, 0f,
                    0f, 0f, 0f, 1f
                )
            )
        }

        @JvmStatic
        public fun rotationY(angle: Float): OmniMatrix4f {
            val c = cos(angle)
            val s = sin(angle)
            return OmniMatrix4f(
                floatArrayOf(
                    c, 0f, -s, 0f,
                    0f, 1f, 0f, 0f,
                    s, 0f, c, 0f,
                    0f, 0f, 0f, 1f
                )
            )
        }

        @JvmStatic
        public fun rotationZ(angle: Float): OmniMatrix4f {
            val c = cos(angle)
            val s = sin(angle)
            return OmniMatrix4f(
                floatArrayOf(
                    c, s, 0f, 0f,
                    -s, c, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 1f
                )
            )
        }

        @JvmStatic
        public fun rotation(axisX: Float, axisY: Float, axisZ: Float, angle: Float): OmniMatrix4f {
            var nx = axisX
            var ny = axisY
            var nz = axisZ
            val axisLen = sqrt(nx * nx + ny * ny + nz * nz)
            require(axisLen > 0f) { "Axis length must be > 0" }
            nx /= axisLen; ny /= axisLen; nz /= axisLen

            val c = cos(angle)
            val s = sin(angle)
            val t = 1f - c
            return OmniMatrix4f(
                floatArrayOf(
                    t * nx * nx + c, t * nx * ny + s * nz, t * nx * nz - s * ny, 0f,
                    t * nx * ny - s * nz, t * ny * ny + c, t * ny * nz + s * nx, 0f,
                    t * nx * nz + s * ny, t * ny * nz - s * nx, t * nz * nz + c, 0f,
                    0f, 0f, 0f, 1f
                )
            )
        }
    }

    public class Builder @JvmOverloads public constructor(item: OmniMatrix4f? = null) {
        private val data = FloatArray(16) { if (it % 5 == 0) 1f else 0f } // Identity

        init {
            if (item != null) {
                for (i in data.indices) {
                    data[i] = item.data[i]
                }
            }
        }

        public fun set(row: Int, column: Int, value: Float): Builder {
            require(row in 0..3 && column in 0..3) { "Index out of bounds: ($row, $column)" }
            data[column * 4 + row] = value
            return this
        }

        public fun m00(value: Float): Builder = set(0, 0, value)
        public fun m01(value: Float): Builder = set(0, 1, value)
        public fun m02(value: Float): Builder = set(0, 2, value)
        public fun m03(value: Float): Builder = set(0, 3, value)
        public fun m10(value: Float): Builder = set(1, 0, value)
        public fun m11(value: Float): Builder = set(1, 1, value)
        public fun m12(value: Float): Builder = set(1, 2, value)
        public fun m13(value: Float): Builder = set(1, 3, value)
        public fun m20(value: Float): Builder = set(2, 0, value)
        public fun m21(value: Float): Builder = set(2, 1, value)
        public fun m22(value: Float): Builder = set(2, 2, value)
        public fun m23(value: Float): Builder = set(2, 3, value)
        public fun m30(value: Float): Builder = set(3, 0, value)
        public fun m31(value: Float): Builder = set(3, 1, value)
        public fun m32(value: Float): Builder = set(3, 2, value)
        public fun m33(value: Float): Builder = set(3, 3, value)

        public fun build(): OmniMatrix4f {
            return OmniMatrix4f(data)
        }
    }

    public var m00: Float
        get() = data[0]
        set(value) {
            data[0] = value
        }
    public var m01: Float
        get() = data[4]
        set(value) {
            data[4] = value
        }
    public var m02: Float
        get() = data[8]
        set(value) {
            data[8] = value
        }
    public var m03: Float
        get() = data[12]
        set(value) {
            data[12] = value
        }
    public var m10: Float
        get() = data[1]
        set(value) {
            data[1] = value
        }
    public var m11: Float
        get() = data[5]
        set(value) {
            data[5] = value
        }
    public var m12: Float
        get() = data[9]
        set(value) {
            data[9] = value
        }
    public var m13: Float
        get() = data[13]
        set(value) {
            data[13] = value
        }
    public var m20: Float
        get() = data[2]
        set(value) {
            data[2] = value
        }
    public var m21: Float
        get() = data[6]
        set(value) {
            data[6] = value
        }
    public var m22: Float
        get() = data[10]
        set(value) {
            data[10] = value
        }
    public var m23: Float
        get() = data[14]
        set(value) {
            data[14] = value
        }
    public var m30: Float
        get() = data[3]
        set(value) {
            data[3] = value
        }
    public var m31: Float
        get() = data[7]
        set(value) {
            data[7] = value
        }
    public var m32: Float
        get() = data[11]
        set(value) {
            data[11] = value
        }
    public var m33: Float
        get() = data[15]
        set(value) {
            data[15] = value
        }

    public val vanilla: Matrix4f
        get() = applyTo(Matrix4f())

    init {
        require(data.size == 16) { "Matrix must have exactly 16 elements, but has ${data.size}" }
    }

    public operator fun get(row: Int, column: Int): Float {
        require(row in 0..3 && column in 0..3) { "Index out of bounds: ($row, $column)" }
        return data[column * 4 + row]
    }

    public fun multiply(other: OmniMatrix4f): OmniMatrix4f {
        val left = this.data
        val right = other.data
        val result = FloatArray(16)

        for (column in 0..3) {
            val r0 = right[column * 4 + 0]
            val r1 = right[column * 4 + 1]
            val r2 = right[column * 4 + 2]
            val r3 = right[column * 4 + 3]

            result[column * 4 + 0] = left[0] * r0 + left[4] * r1 + left[8] * r2 + left[12] * r3
            result[column * 4 + 1] = left[1] * r0 + left[5] * r1 + left[9] * r2 + left[13] * r3
            result[column * 4 + 2] = left[2] * r0 + left[6] * r1 + left[10] * r2 + left[14] * r3
            result[column * 4 + 3] = left[3] * r0 + left[7] * r1 + left[11] * r2 + left[15] * r3
        }

        return OmniMatrix4f(result)
    }

    public fun translate(x: Float, y: Float, z: Float): OmniMatrix4f {
        val m00 = data[0]; val m10 = data[1]; val m20 = data[2]; val m30 = data[3]
        val m01 = data[4]; val m11 = data[5]; val m21 = data[6]; val m31 = data[7]
        val m02 = data[8]; val m12 = data[9]; val m22 = data[10]; val m32 = data[11]
        val m03 = data[12]; val m13 = data[13]; val m23 = data[14]; val m33 = data[15]

        data[12] = m00 * x + m01 * y + m02 * z + m03
        data[13] = m10 * x + m11 * y + m12 * z + m13
        data[14] = m20 * x + m21 * y + m22 * z + m23
        data[15] = m30 * x + m31 * y + m32 * z + m33

        return this
    }

    public fun scale(x: Float, y: Float, z: Float): OmniMatrix4f {
        data[0] *= x; data[1] *= x; data[2] *= x; data[3] *= x
        data[4] *= y; data[5] *= y; data[6] *= y; data[7] *= y
        data[8] *= z; data[9] *= z; data[10] *= z; data[11] *= z

        return this
    }

    public fun rotateX(angle: Float): OmniMatrix4f {
        val c = cos(angle)
        val s = sin(angle)

        val c1r0 = data[4];  val c1r1 = data[5];  val c1r2 = data[6];  val c1r3 = data[7]
        val c2r0 = data[8];  val c2r1 = data[9];  val c2r2 = data[10]; val c2r3 = data[11]

        // new col1 =  c*old col1 + s*old col2
        data[4]  = c * c1r0 + s * c2r0
        data[5]  = c * c1r1 + s * c2r1
        data[6]  = c * c1r2 + s * c2r2
        data[7]  = c * c1r3 + s * c2r3
        // new col2 = -s*old col1 + c*old col2
        data[8]  = -s * c1r0 + c * c2r0
        data[9]  = -s * c1r1 + c * c2r1
        data[10] = -s * c1r2 + c * c2r2
        data[11] = -s * c1r3 + c * c2r3
        return this
    }

    public fun rotateY(angle: Float): OmniMatrix4f {
        val c = cos(angle)
        val s = sin(angle)

        val c0r0 = data[0];  val c0r1 = data[1];  val c0r2 = data[2];  val c0r3 = data[3]
        val c2r0 = data[8];  val c2r1 = data[9];  val c2r2 = data[10]; val c2r3 = data[11]

        // new col0 =  c*old col0 + s*old col2
        data[0]  = c * c0r0 + s * c2r0
        data[1]  = c * c0r1 + s * c2r1
        data[2]  = c * c0r2 + s * c2r2
        data[3]  = c * c0r3 + s * c2r3
        // new col2 = -s*old col0 + c*old col2
        data[8]  = -s * c0r0 + c * c2r0
        data[9]  = -s * c0r1 + c * c2r1
        data[10] = -s * c0r2 + c * c2r2
        data[11] = -s * c0r3 + c * c2r3
        return this
    }

    public fun rotateZ(angle: Float): OmniMatrix4f {
        val c = cos(angle)
        val s = sin(angle)

        val c0r0 = data[0];  val c0r1 = data[1];  val c0r2 = data[2];  val c0r3 = data[3]
        val c1r0 = data[4];  val c1r1 = data[5];  val c1r2 = data[6];  val c1r3 = data[7]

        data[0] = c * c0r0 + s * c1r0
        data[1] = c * c0r1 + s * c1r1
        data[2] = c * c0r2 + s * c1r2
        data[3] = c * c0r3 + s * c1r3
        data[4] = -s * c0r0 + c * c1r0
        data[5] = -s * c0r1 + c * c1r1
        data[6] = -s * c0r2 + c * c1r2
        data[7] = -s * c0r3 + c * c1r3
        return this
    }

    public fun rotate(axisX: Float, axisY: Float, axisZ: Float, angle: Float): OmniMatrix4f {
        val r = rotation(axisX, axisY, axisZ, angle)
        postMultiplyInPlace(r.data)
        return this
    }

    public fun transpose(): OmniMatrix4f {
        val transposed = FloatArray(16)
        for (row in 0..3) for (column in 0..3) {
            transposed[column * 4 + row] = data[row * 4 + column]
        }

        return OmniMatrix4f(transposed)
    }

    public fun transformPosition(x: Float, y: Float, z: Float): FloatArray {
        val xPrime = data[0] * x + data[4] * y + data[8] * z + data[12]
        val yPrime = data[1] * x + data[5] * y + data[9] * z + data[13]
        val zPrime = data[2] * x + data[6] * y + data[10] * z + data[14]
        val wPrime = data[3] * x + data[7] * y + data[11] * z + data[15]
        return floatArrayOf(xPrime, yPrime, zPrime, wPrime)
    }

    public fun transformDirection(x: Float, y: Float, z: Float): OmniVector3f {
        val xPrime = data[0] * x + data[4] * y + data[8] * z
        val yPrime = data[1] * x + data[5] * y + data[9] * z
        val zPrime = data[2] * x + data[6] * y + data[10] * z
        return OmniVector3f(xPrime, yPrime, zPrime)
    }

    public fun deepCopy(): OmniMatrix4f {
        return OmniMatrix4f(data.copyOf())
    }

    public fun toArray(): FloatArray {
        return data.copyOf()
    }

    public fun toRowMajor(): FloatArray {
        val out = FloatArray(16)
        for (r in 0..3) for (c in 0..3) {
            out[r * 4 + c] = data[c * 4 + r]
        }

        return out
    }

    public fun applyTo(dest: Matrix4f): Matrix4f {
        //#if MC >= 1.19.4
        return dest.set(data)
        //#elseif MC >= 1.17.1 || MC <= 1.12.2
        //$$ val buffer = BufferUtils.createFloatBuffer(16)
        //$$ buffer.put(data)
        //$$ (buffer as java.nio.Buffer).flip()
        //#if MC >= 1.17.1
        //$$ dest.load(buffer)
        //#else
        //$$ // need to do some black magic here because LWJGL 2's Matrix4f has a different name than what we leave it on in 1.17.1
        //$$ dest.load(buffer)
        //#endif
        //$$ return dest
        //#else
        //$$ val accessor = dest as Mixin_Matrix4fSetters
        //$$ accessor.setM00(data[0]); accessor.setM01(data[4]); accessor.setM02(data[8]); accessor.setM03(data[12])
        //$$ accessor.setM10(data[1]); accessor.setM11(data[5]); accessor.setM12(data[9]); accessor.setM13(data[13])
        //$$ accessor.setM20(data[2]); accessor.setM21(data[6]); accessor.setM22(data[10]); accessor.setM23(data[14])
        //$$ accessor.setM30(data[3]); accessor.setM31(data[7]); accessor.setM32(data[11]); accessor.setM33(data[15])
        //$$ return dest
        //#endif
    }

    public fun applyFrom(other: OmniMatrix4f): OmniMatrix4f {
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
            append("OmniMatrix4f(\n")
            for (row in 0..3) {
                append("  ")
                for (col in 0..3) {
                    append(String.format("% .4f", this@OmniMatrix4f[row, col]))
                    if (col < 3) append(", ")
                }
                append("\n")
            }
            append(")")
        }
    }

    private fun postMultiplyInPlace(rhs: FloatArray) {
        val left = this.data
        val right = rhs
        val result = FloatArray(16)

        for (col in 0..3) {
            val b0 = right[col * 4 + 0]
            val b1 = right[col * 4 + 1]
            val b2 = right[col * 4 + 2]
            val b3 = right[col * 4 + 3]

            result[col * 4 + 0] = left[0] * b0 + left[4] * b1 + left[8]  * b2 + left[12] * b3
            result[col * 4 + 1] = left[1] * b0 + left[5] * b1 + left[9]  * b2 + left[13] * b3
            result[col * 4 + 2] = left[2] * b0 + left[6] * b1 + left[10] * b2 + left[14] * b3
            result[col * 4 + 3] = left[3] * b0 + left[7] * b1 + left[11] * b2 + left[15] * b3
        }

        for (i in 0 until 16) {
            data[i] = result[i]
        }
    }
}

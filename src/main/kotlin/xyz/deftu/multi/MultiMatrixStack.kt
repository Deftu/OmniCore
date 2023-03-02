package xyz.deftu.multi

//#if MC>=11500
import net.minecraft.util.math.MathHelper
import net.minecraft.client.util.math.MatrixStack
//#endif

//#if MC>=11700
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC>=11903
import org.joml.Quaternionf
//#elseif MC>=11400
//$$ import net.minecraft.util.math.Quaternion
//#else
//$$ import org.lwjgl.util.vector.Vector3f
//#endif

//#if MC>=10809 && MC<11700
//#if MC<11500
//$$ import net.minecraft.client.renderer.GLAllocation
//#else
//$$ import net.minecraft.client.util.GlAllocationUtils
//#endif
//$$ import java.nio.FloatBuffer
//$$ import java.nio.Buffer
//#endif

import java.util.*
import kotlin.math.*
import org.lwjgl.opengl.GL11
import com.mojang.blaze3d.platform.GlStateManager
import org.joml.Matrix4f
import org.joml.Matrix3f

class MultiMatrixStack private constructor(
    private val stack: Deque<StackEntry>
) {
    companion object {
        //#if MC<11700
        //$$ private val MATRIX_BUFFER: FloatBuffer = createFloatBuffer(16)
        //$$
        //$$ private fun createFloatBuffer(capacity: Int): FloatBuffer {
            //#if MC>=11500
            //$$ return GlAllocationUtils.allocateFloatBuffer(capacity)
            //#else
            //$$ return GLAllocation.createDirectFloatBuffer(capacity)
            //#endif
        //$$ }
        //#endif
    }

    constructor() : this(ArrayDeque<StackEntry>().apply {
        add(StackEntry(
            Matrix4f(),
            Matrix3f()
        ))
    })

    //#if MC>=11600
    constructor(entry: MatrixStack.Entry) : this(ArrayDeque<StackEntry>().apply {
        add(StackEntry(
            entry.positionMatrix,
            entry.normalMatrix
        ))
    })

    constructor(stack: MatrixStack) : this(stack.peek())

    fun toVanillaStack() = peek().toVanillaStack()
    //#endif

    fun scale(
        x: Float,
        y: Float,
        z: Float
    ) {
        if (
            x == 1f &&
            y == 1f &&
            z == 1f
        ) return
        stack.last.run {
            //#if MC>=11903
            matrix.scale(x, y, z)
            //#elseif MC>=11500
            //$$ matrix.multiply(Matrix4f.scale(x, y, z))
            //#elseif MC>=11400
            //$$ matrix.mul(Matrix4f.scale(x, y, z))
            //#else
            //$$ Matrix4f.scale(Vector3f(x, y, z), matrix, matrix)
            //#endif

            if (x == y && y == z) {
                if (0f > x) {
                    //#if MC>=11903
                    normal.scale(-1f)
                    //#elseif MC>=11400
                    //$$ normal.multiply(-1f)
                    //#else
                    //$$ Matrix3f.negate(normal, normal)
                    //#endif
                } else {
                }
            } else {
                val ix = 1f / x
                val iy = 1f / y
                val iz = 1f / z

                //#if MC>=11400
                val rt = MathHelper.fastInverseCbrt(ix * iy * iz)
                //#else
                //$$ val rt = Math.cbrt((ix * iy * iz).toDouble()).toFloat()
                //#endif

                //#if MC>=11903
                normal.scale(rt * ix, rt * iy, rt * iz)
                //#elseif MC>=11400
                //$$ normal.multiply(Matrix3f.scale(rt * ix, rt * iy, rt * iz))
                //#else
                //$$ val scale = Matrix3f()
                //$$ scale.m00 = rt * ix
                //$$ scale.m11 = rt * iy
                //$$ scale.m22 = rt * iz
                //$$ Matrix3f.mul(scale, normal, normal)
                //#endif
            }
        }
    }

    fun scale(
        x: Double,
        y: Double,
        z: Double
    ) = scale(x.toFloat(), y.toFloat(), z.toFloat())

    fun translate(
        x: Float,
        y: Float,
        z: Float
    ) {
        if (
            x == 0f &&
            y == 0f &&
            z == 0f
        ) return
        stack.last.run {
            //#if MC>=11903
            matrix.translate(x, y, z)
            //#elseif MC>=11400
            //$$ matrix.multiply(Matrix4f.translate(x, y, z))
            //#else
            //$$ Matrix4f.translate(Vector3f(x, y, z), matrix, matrix)
            //#endif
        }
    }

    fun translate(
        x: Double,
        y: Double,
        z: Double
    ) = translate(x.toFloat(), y.toFloat(), z.toFloat())

    @JvmOverloads
    fun rotate(
        angle: Float,
        x: Float,
        y: Float,
        z: Float,
        degrees: Boolean = true
    ) {
        if (angle == 0f) return
        stack.last.run {
            val radians = if (degrees) Math.toRadians(angle.toDouble()).toFloat() else angle
            //#if MC>=11903
            val quaternion = Quaternionf().rotateAxis(radians, x, y, z)
            matrix.rotate(quaternion)
            normal.rotate(quaternion)
            //#elseif MC>=11400
            //$$ matrix.multiply(Quaternion(x, y, z, radians))
            //#else
            //$$ val axis = Vector3f(x, y, z)
            //$$ Matrix4f.rotate(radians, axis, matrix, matrix)
            //$$
            //$$ fun makeRotationMatrix(angle: Float, axis: Vector3f) = Matrix3f().apply {
            //$$     val c = cos(angle)
            //$$     val s = sin(angle)
            //$$     val oneMinusC = 1 - c
            //$$     val xx = axis.x * axis.x
            //$$     val xy = axis.x * axis.y
            //$$     val xz = axis.x * axis.z
            //$$     val yy = axis.y * axis.y
            //$$     val yz = axis.y * axis.z
            //$$     val zz = axis.z * axis.z
            //$$     val xs = axis.x * s
            //$$     val ys = axis.y * s
            //$$     val zs = axis.z * s
            //$$
            //$$     m00 = xx * oneMinusC + c
            //$$     m01 = xy * oneMinusC + zs
            //$$     m02 = xz * oneMinusC - ys
            //$$     m10 = xy * oneMinusC - zs
            //$$     m11 = yy * oneMinusC + c
            //$$     m12 = yz * oneMinusC + xs
            //$$     m20 = xz * oneMinusC + ys
            //$$     m21 = yz * oneMinusC - xs
            //$$     m22 = zz * oneMinusC + c
            //$$ }
            //$$ Matrix3f.mul(normal, makeRotationMatrix(radians, axis), normal)
            //#endif
        }
    }

    @JvmOverloads
    fun rotate(
        angle: Double,
        x: Double,
        y: Double,
        z: Double,
        degrees: Boolean = true
    ) = rotate(angle.toFloat(), x.toFloat(), y.toFloat(), z.toFloat(), degrees)

    fun push() = stack.addLast(stack.last.deepCopy())
    fun pop() = stack.removeLast()
    fun peek() = stack.last
    fun isEmpty() = stack.size == 1

    fun copy() = MultiMatrixStack(stack.map { it.deepCopy() }.toCollection(ArrayDeque()))

    fun applyToGlobalState() {
        //#if MC>=11700
        //#if MC>=11800
        RenderSystem.getModelViewStack().multiplyPositionMatrix(stack.last.matrix)
        //#else
        //$$ RenderSystem.getModelViewStack().method_34425(stack.last.matrix)
        //#endif
        RenderSystem.applyModelViewMatrix()
        //#else
        //#if MC<11600
        //#if MC>=11500
        //$$ stack.last.matrix.writeToBuffer(MATRIX_BUFFER)
        //#else
        //$$ stack.last.matrix.store(MATRIX_BUFFER)
        //#endif
        //#else
        //$$ stack.last.matrix.writeRowFirst(MATRIX_BUFFER)
        //#endif
        //$$ // Explicit cast to Buffer required so we do not use the JDK9+ override in FloatBuffer
        //$$ (MATRIX_BUFFER as Buffer).rewind()
        //#if MC>=11500
        //$$ GL11.glMultMatrixf(MATRIX_BUFFER)
        //#else
        //$$ GL11.glMultMatrix(MATRIX_BUFFER)
        //#endif
        //#endif
    }

    fun replaceGlobalState() {
        //#if MC>=11700
        RenderSystem.getModelViewStack().loadIdentity()
        //#else
        //$$ GL11.glLoadIdentity()
        //#endif
        applyToGlobalState()
    }

    private inline fun <R> withGlobalStackPushed(block: () -> R) : R {
        //#if MC>=11700
        val stack = RenderSystem.getModelViewStack()
        stack.push()
        //#else
        //$$ GlStateManager.pushMatrix()
        //#endif
        return block().also {
            //#if MC>=11700
            stack.pop()
            RenderSystem.applyModelViewMatrix()
            //#else
            //$$ GlStateManager.popMatrix()
            //#endif
        }
    }

    fun <R> runWithGlobalState(block: () -> R): R  = withGlobalStackPushed {
        applyToGlobalState()
        block()
    }

    fun runWithGlobalState(block: Runnable) = runWithGlobalState { block.run() }

    fun <R> runReplacingGlobalState(block: () -> R): R = withGlobalStackPushed {
        replaceGlobalState()
        block()
    }

    fun runReplacingGlobalState(block: Runnable) = runReplacingGlobalState { block.run() }

    data class StackEntry(
        val matrix: Matrix4f,
        val normal: Matrix3f
    ) {
        //#if MC>=11600
        fun toVanillaStack() = MatrixStack().also { stack ->
        //#if MC>=11903
            stack.peek().positionMatrix.mul(matrix)
            stack.peek().normalMatrix.mul(normal)
        //#else
        //$$     stack.peek().positionMatrix.multiply(matrix)
        //$$     stack.peek().normalMatrix.multiply(normal)
        //#endif
        }
        //#endif

        fun deepCopy(): StackEntry {
            //#if MC>=11903
            return StackEntry(Matrix4f(matrix), Matrix3f(normal))
            //#elseif MC>=11500
            //$$ return StackEntry(matrix.copy(), normal.copy())
            //#else
            //$$ return StackEntry(Matrix4f.load(matrix, null), Matrix3f.load(normal, null))
            //#endif
        }
    }
}

@file:Suppress("MemberVisibilityCanBePrivate", "UnusedImport")

package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import java.util.*
import org.joml.Matrix4f
import org.joml.Matrix3f

//#if MC >= 1.21.6
//$$ import org.joml.Matrix3x2f
//#endif

//#if MC >= 1.20.1
import net.minecraft.client.gui.DrawContext
//#endif

//#if MC >= 1.16.5
import net.minecraft.util.math.MathHelper
import net.minecraft.client.util.math.MatrixStack
//#endif

//#if MC >= 1.17.1
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC >= 1.19.3
import org.joml.Quaternionf
//#elseif MC >= 1.16.5
//$$ import com.mojang.math.Quaternion
//#else
//$$ import org.lwjgl.util.vector.Vector3f
//#endif

//#if MC >= 1.8.9 && MC < 1.17.1
//$$ import net.minecraft.client.util.GlAllocationUtils
//$$ import java.nio.FloatBuffer
//$$ import java.nio.Buffer
//#endif

//#if MC <= 1.16.5
//$$ import org.lwjgl.opengl.GL11
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

//#if MC <= 1.12.2
//$$ import kotlin.math.*
//#endif

/**
 * Provides a way to manipulate the current rendering state in a way that is compatible with both the vanilla matrix stack in modern versions and the legacy OpenGL global matrix stack.
 *
 * @since 0.1.0
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public class OmniMatrixStack private constructor(
    private val stack: Deque<StackEntry>
) {

    public companion object {

        @JvmField
        @GameSide(Side.CLIENT)
        public val EMPTY: OmniMatrixStack = OmniMatrixStack()

        //#if MC < 1.17
        //$$ private val MATRIX_BUFFER: FloatBuffer = createFloatBuffer(16)
        //$$
        //$$ private fun createFloatBuffer(capacity: Int): FloatBuffer {
        //$$     return GlAllocationUtils.allocateFloatBuffer(capacity)
        //$$ }
        //#endif

        /**
         * Creates a new [OmniMatrixStack] instance which uses whatever is the most appropriate current-version matrix holder.
         *
         * @since 0.18.0
         * @author Deftu
         */
        @JvmStatic
        public fun vanilla(
            //#if MC >= 1.20.1
            ctx: DrawContext
            //#elseif MC >= 1.16.5
            //$$ ctx: MatrixStack
            //#endif
        ): OmniMatrixStack {
            //#if MC >= 1.20.1
            return OmniMatrixStack(ctx.matrices)
            //#elseif MC >= 1.16.5
            //$$ return OmniMatrixStack(ctx)
            //#else
            //$$ return OmniMatrixStack()
            //#endif
        }

        //#if MC >= 1.20.1
        /**
         * Creates a new [OmniMatrixStack] instance using the provided [MatrixStack] specifically.
         *
         * @since 0.18.0
         * @author Deftu
         */
        @JvmStatic
        public fun vanilla(ctx: MatrixStack): OmniMatrixStack {
            return OmniMatrixStack(ctx)
        }
        //#endif

    }

    /**
     * Represents a single entry in a matrix stack.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public data class StackEntry(
        val matrix: Matrix4f,
        val normal: Matrix3f
    ) {

        //#if MC >= 1.16
        /**
         * Converts this [StackEntry] to a vanilla [MatrixStack] instance.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @GameSide(Side.CLIENT)
        public fun toVanillaStack(): MatrixStack = MatrixStack().also { stack ->
            //#if MC >= 1.19.3
            stack.peek().positionMatrix.mul(matrix)
            stack.peek().normalMatrix.mul(normal)
            //#else
            //$$     stack.last().pose().multiply(matrix)
            //$$     stack.last().normal().mul(normal)
            //#endif
        }
        //#endif

        /**
         * Creates a deep copy of this [StackEntry].
         *
         * @since 0.1.0
         * @author Deftu
         */
        @GameSide(Side.CLIENT)
        public fun deepCopy(): StackEntry {
            //#if MC >= 1.19.3
            return StackEntry(Matrix4f(matrix), Matrix3f(normal))
            //#elseif MC >= 1.15
            //$$ return StackEntry(matrix.copy(), normal.copy())
            //#else
            //$$ return StackEntry(Matrix4f.load(matrix, null), Matrix3f.load(normal, null))
            //#endif
        }

    }

    /**
     * Returns whether the stack is empty.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public val isEmpty: Boolean
        get() = stack.size == 1

    /**
     * Creates a new [OmniMatrixStack] instance with an empty stack.
     *
     * @since 0.1.0
     * @author Deftu
     */
    public constructor() : this(ArrayDeque<StackEntry>().apply {
        add(StackEntry(
            Matrix4f(),
            Matrix3f()
        ))
    })

    //#if MC >= 1.16.5
    /**
     * Creates a new [OmniMatrixStack] instance with the values contained within the provided vanilla [MatrixStack.Entry].
     *
     * @since 0.1.0
     * @author Deftu
     */
    public constructor(entry: MatrixStack.Entry) : this(ArrayDeque<StackEntry>().apply {
        add(StackEntry(
            entry.positionMatrix,
            entry.normalMatrix
        ))
    })

    /**
     * Creates a new [OmniMatrixStack] instance with the values contained within the provided vanilla [MatrixStack].
     *
     * @since 0.1.0
     * @author Deftu
     */
    public constructor(stack: MatrixStack) : this(stack.peek())

    //#if MC >= 1.21.6
    //$$ public constructor(stack: Matrix3x2f) : this() {
    //$$     with(peek().matrix) {
    //$$         m00(stack.m00)
    //$$         m01(stack.m01)
    //$$         m10(stack.m10)
    //$$         m11(stack.m11)
    //$$         m30(stack.m20)
    //$$         m31(stack.m21)
    //$$     }
    //$$ }
    //#endif

    /**
     * Converts the [OmniMatrixStack] to a vanilla [MatrixStack] instance.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun toVanillaStack(): MatrixStack {
        return peek().toVanillaStack()
    }

    //#if MC >= 1.21.6
    //$$ public fun to3x2fJoml(dest: Matrix3x2f = Matrix3x2f()): Matrix3x2f {
    //$$     return dest.apply {
    //$$         val ours = peek().matrix
    //$$         set(
    //$$             ours.m00(), ours.m01(),
    //$$             ours.m10(), ours.m11(),
    //$$             ours.m30(), ours.m31()
    //$$         )
    //$$     }
    //$$ }
    //#endif
    //#endif

    /**
     * Scales the current matrix by the provided values.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun scale(
        x: Float,
        y: Float,
        z: Float
    ) {
        if (
            x == 1f &&
            y == 1f &&
            z == 1f
        ) {
            return
        }

        return stack.last.run {
            //#if MC >= 1.19.3
            matrix.scale(x, y, z)
            //#elseif MC >= 1.15
            //$$ matrix.multiply(Matrix4f.createScaleMatrix(x, y, z))
            //#elseif MC >= 1.14
            //$$ matrix.mul(Matrix4f.scale(x, y, z))
            //#else
            //$$ Matrix4f.scale(Vector3f(x, y, z), matrix, matrix)
            //#endif

            if (x == y && y == z) {
                if (0f > x) {
                    //#if MC >= 1.19.3
                    normal.scale(-1f)
                    //#elseif MC >= 1.14
                    //$$ normal.mul(-1f)
                    //#else
                    //$$ Matrix3f.negate(normal, normal)
                    //#endif
                }
            } else {
                val ix = 1f / x
                val iy = 1f / y
                val iz = 1f / z

                //#if MC >= 1.14
                val rt = MathHelper.fastInverseCbrt(ix * iy * iz)
                //#else
                //$$ val rt = Math.cbrt((ix * iy * iz).toDouble()).toFloat()
                //#endif

                //#if MC >= 1.19.3
                normal.scale(rt * ix, rt * iy, rt * iz)
                //#elseif MC >= 1.14
                //$$ normal.mul(Matrix3f.createScaleMatrix(rt * ix, rt * iy, rt * iz))
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

    /**
     * Scales the current matrix by the provided values.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun scale(
        x: Double,
        y: Double,
        z: Double
    ) {
        scale(x.toFloat(), y.toFloat(), z.toFloat())
    }

    /**
     * Translates the current matrix by the provided values.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun translate(
        x: Float,
        y: Float,
        z: Float
    ) {
        if (
            x == 0f &&
            y == 0f &&
            z == 0f
        ) {
            return
        }

        stack.last.run {
            //#if MC >= 1.19.3
            matrix.translate(x, y, z)
            //#elseif MC >= 1.14
            //$$ matrix.multiply(Matrix4f.createTranslateMatrix(x, y, z))
            //#else
            //$$ Matrix4f.translate(Vector3f(x, y, z), matrix, matrix)
            //#endif
        }
    }

    /**
     * Translates the current matrix by the provided values.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun translate(
        x: Double,
        y: Double,
        z: Double
    ) {
        translate(x.toFloat(), y.toFloat(), z.toFloat())
    }

    /**
     * Rotates the current matrix by the provided values.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmOverloads
    @GameSide(Side.CLIENT)
    public fun rotate(
        angle: Float,
        x: Float,
        y: Float,
        z: Float,
        degrees: Boolean = true
    ) {
        if (angle == 0f) {
            return
        }

        stack.last.run {
            val radians = if (degrees) Math.toRadians(angle.toDouble()).toFloat() else angle
            //#if MC >= 1.19.3
            val quaternion = Quaternionf().rotateAxis(radians, x, y, z)
            matrix.rotate(quaternion)
            normal.rotate(quaternion)
            //#elseif MC >= 1.14
            //$$ matrix.multiply(Quaternion(x, y, z, radians))
            //#else
            //$$ val axis = Vector3f(x, y, z)
            //$$ Matrix4f.rotate(radians, axis, matrix, matrix)
            //$$
            //$$ fun createRotationMatrix(angle: Float, axis: Vector3f) = Matrix3f().apply {
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
            //$$
            //$$ Matrix3f.mul(normal, createRotationMatrix(radians, axis), normal)
            //#endif
        }
    }


    /**
     * Rotates the current matrix by the provided values.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmOverloads
    @GameSide(Side.CLIENT)
    public fun rotate(
        angle: Double,
        x: Double,
        y: Double,
        z: Double,
        degrees: Boolean = true
    ) {
        rotate(angle.toFloat(), x.toFloat(), y.toFloat(), z.toFloat(), degrees)
    }

    /**
     * Pushes a new matrix onto the stack, copying the current matrix.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun push() {
        stack.addLast(stack.last.deepCopy())
    }

    /**
     * Pops the current matrix from the stack.
     *
     * @throws IllegalStateException if the stack is empty.
     * @return The popped matrix.
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun pop(): StackEntry {
        return stack.removeLast()
    }

    /**
     * Peeks at the current matrix on the stack.
     *
     * @throws IllegalStateException if the stack is empty.
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun peek(): StackEntry {
        return stack.last
    }

    /**
     * Creates an identical, independent copy of this [OmniMatrixStack].
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun copy(): OmniMatrixStack {
        return OmniMatrixStack(stack.map { entry ->
            entry.deepCopy()
        }.toCollection(ArrayDeque()))
    }

    /**
     * Applies the current matrix to the global rendering state.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun applyToGlobalState() {
        //#if MC >= 1.17
        //#if MC >= 1.20.5
        //$$ RenderSystem.getModelViewStack().mul(stack.last.matrix)
        //#elseif MC >= 1.17.1
        RenderSystem.getModelViewStack().multiplyPositionMatrix(stack.last.matrix)
        //#else
        //$$ RenderSystem.getModelViewStack().method_34425(stack.last.matrix)
        //#endif
        //#if MC <= 1.21.1
        RenderSystem.applyModelViewMatrix()
        //#endif
        //#else
        //#if MC < 1.16
        //#if MC >= 1.15
        //$$ stack.last.matrix.writeToBuffer(MATRIX_BUFFER)
        //#else
        //$$ stack.last.matrix.store(MATRIX_BUFFER)
        //#endif
        //#else
        //$$ stack.last.matrix.writeRowFirst(MATRIX_BUFFER)
        //#endif
        //$$ // Explicit cast to Buffer required, so we do not use the JDK9+ override in FloatBuffer
        //$$ (MATRIX_BUFFER as Buffer).rewind()
        //#if MC >= 1.15
        //$$ GL11.glMultMatrixf(MATRIX_BUFFER)
        //#else
        //$$ GL11.glMultMatrix(MATRIX_BUFFER)
        //#endif
        //#endif
    }

    /**
     * Replaces the global rendering state with the current matrix.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun replaceGlobalState() {
        //#if MC >= 1.20.5
        //$$ RenderSystem.getModelViewStack().identity()
        //#elseif MC >= 1.17
        RenderSystem.getModelViewStack().loadIdentity()
        //#else
        //$$ GL11.glLoadIdentity()
        //#endif
        applyToGlobalState()
    }

    private inline fun <R> withGlobalStackPushed(block: () -> R) : R {
        //#if MC >= 1.17
        val stack = RenderSystem.getModelViewStack()
        //#if MC >= 1.20.5
        //$$ stack.pushMatrix()
        //#else
        stack.push()
        //#endif
        //#else
        //$$ GlStateManager.pushMatrix()
        //#endif
        return block().also {
            //#if MC >= 1.17
            //#if MC >= 1.20.5
            //$$ stack.popMatrix()
            //#else
            stack.pop()
            //#endif
            //#if MC <= 1.21.1
            RenderSystem.applyModelViewMatrix()
            //#endif
            //#else
            //$$ GlStateManager.popMatrix()
            //#endif
        }
    }

    /**
     * Runs the provided block with the current matrix applied to the global rendering state.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun <R> runWithGlobalState(block: () -> R): R {
        return withGlobalStackPushed {
            applyToGlobalState()
            block()
        }
    }

    /**
     * Runs the provided block with the current matrix applied to the global rendering state.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun runWithGlobalState(block: Runnable) {
        runWithGlobalState {
            block.run()
        }
    }

    /**
     * Runs the provided block with the current matrix replacing the global rendering state.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun <R> runReplacingGlobalState(block: () -> R): R {
        return withGlobalStackPushed {
            replaceGlobalState()
            block()
        }
    }

    /**
     * Runs the provided block with the current matrix replacing the global rendering state.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @GameSide(Side.CLIENT)
    public fun runReplacingGlobalState(block: Runnable) {
        runReplacingGlobalState {
            block.run()
        }
    }

    @GameSide(Side.CLIENT)
    public fun using(block: OmniMatrixStack.() -> Unit) {
        push()

        try {
            block()
        } finally {
            pop()
        }
    }

}

package dev.deftu.omnicore.internal.client.render.stack

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import org.joml.Matrix3f
import org.joml.Matrix3x2f
import org.joml.Matrix4f
import org.joml.Quaternionf
import java.util.ArrayDeque
import java.util.Deque

//#if MC >= 1.20.1
import net.minecraft.client.gui.DrawContext
//#endif

//#if MC >= 1.17.1
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper

//#endif

//#if MC <= 1.16.5
//$$ import net.minecraft.client.util.GlAllocationUtils
//$$ import java.nio.FloatBuffer
//$$ import java.nio.Buffer
//#endif

public class OmniMatrixStackImpl private constructor(private val stack: Deque<OmniMatrixStack.Entry>) : OmniMatrixStack {
    private companion object {
        //#if MC <= 1.16.5
        //$$ private val MATRIX_BUFFER: FloatBuffer = createFloatBuffer(16)
        //$$
        //$$ private fun createFloatBuffer(capacity: Int): FloatBuffer {
        //$$     return GlAllocationUtils.allocateFloatBuffer(capacity)
        //$$ }
        //#endif
    }

    override val current: OmniMatrixStack.Entry
        get() = stack.last

    override val isEmpty: Boolean
        get() = stack.isEmpty()

    public constructor() : this(ArrayDeque<OmniMatrixStack.Entry>().apply {
        add(OmniMatrixStack.Entry(Matrix4f(), Matrix3f()))
    })

    //#if MC >= 1.16.5
    public constructor(stack: MatrixStack.Entry) : this(ArrayDeque<OmniMatrixStack.Entry>().apply {
        add(OmniMatrixStack.Entry(stack.positionMatrix, stack.normalMatrix))
    })

    public constructor(stack: MatrixStack) : this(stack.peek())
    //#endif

    //#if MC >= 1.20.1
    public constructor(graphics: DrawContext) : this(graphics.matrices)
    //#endif

    //#if MC >= 1.21.6
    public constructor(stack: Matrix3x2f) : this() {
        with(current.positionMatrix) {
            m00(stack.m00)
            m01(stack.m01)
            m10(stack.m10)
            m11(stack.m11)
            m30(stack.m20)
            m31(stack.m21)
        }
    }
    //#endif

    override fun deepCopy(): OmniMatrixStack {
        return OmniMatrixStackImpl(ArrayDeque(stack.map(OmniMatrixStack.Entry::deepCopy)))
    }

    override fun push() {
        stack.addLast(current.deepCopy())
    }

    override fun pop(): OmniMatrixStack.Entry {
        if (stack.size <= 1) {
            throw IllegalStateException("Cannot pop the last matrix stack entry")
        }

        return stack.removeLast()
    }

    override fun translate(x: Float, y: Float, z: Float) {
        if (
            x == 0f &&
            y == 0f &&
            z == 0f
        ) {
            return
        }

        with(current) {
            //#if MC >= 1.19.3
            positionMatrix.translate(x, y, z)
            //#elseif MC >= 1.14
            //$$ positionMatrix.multiply(Matrix4f.createTranslateMatrix(x, y, z))
            //#else
            //$$ Matrix4f.translate(Vector3f(x, y, z), positionMatrix, positionMatrix)
            //#endif
        }
    }

    override fun scale(x: Float, y: Float, z: Float) {
        if (
            x == 1f &&
            y == 1f &&
            z == 1f
        ) {
            return
        }

        with(current) {
            //#if MC >= 1.19.3
            positionMatrix.scale(x, y, z)
            //#elseif MC >= 1.15
            //$$ positionMatrix.multiply(Matrix4f.createScaleMatrix(x, y, z))
            //#elseif MC >= 1.14
            //$$ positionMatrix.mul(Matrix4f.scale(x, y, z))
            //#else
            //$$ Matrix4f.scale(Vector3f(x, y, z), positionMatrix, positionMatrix)
            //#endif

            if (x == y && y == z) {
                if (0f > x) {
                    //#if MC >= 1.19.3
                    normalMatrix.scale(-1f)
                    //#elseif MC >= 1.14
                    //$$ normalMatrix.mul(-1f)
                    //#else
                    //$$ Matrix3f.negate(normalMatrix, normalMatrix)
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
                normalMatrix.scale(rt * ix, rt * iy, rt * iz)
                //#elseif MC >= 1.14
                //$$ normalMatrix.mul(Matrix3f.createScaleMatrix(rt * ix, rt * iy, rt * iz))
                //#else
                //$$ val scale = Matrix3f()
                //$$ scale.m00 = rt * ix
                //$$ scale.m11 = rt * iy
                //$$ scale.m22 = rt * iz
                //$$ Matrix3f.mul(scale, normalMatrix, normalMatrix)
                //#endif
            }
        }
    }

    override fun rotate(
        angle: Float,
        axisX: Float,
        axisY: Float,
        axisZ: Float,
        isDegrees: Boolean
    ) {
        if (angle == 0f) {
            return
        }

        with(current) {
            val radians = if (isDegrees) Math.toRadians(angle.toDouble()).toFloat() else angle
            //#if MC >= 1.19.3
            val quaternion = Quaternionf().rotateAxis(radians, axisX, axisY, axisZ)
            positionMatrix.rotate(quaternion)
            normalMatrix.rotate(quaternion)
            //#elseif MC >= 1.14
            //$$ positionMatrix.multiply(Quaternion(x, y, z, radians))
            //#else
            //$$ val axis = Vector3f(axisX, axisY, axisZ)
            //$$ Matrix4f.rotate(radians, axis, positionMatrix, positionMatrix)
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
            //$$ Matrix3f.mul(normalMatrix, createRotationMatrix(radians, axis), normalMatrix)
            //#endif
        }
    }

    override fun rotate(quaternion: Quaternionf) {
        with(current) {
            //#if MC >= 1.19.3
            positionMatrix.rotate(quaternion)
            normalMatrix.rotate(quaternion)
            //#elseif MC >= 1.14
            //$$ positionMatrix.multiply(quaternion)
            //#else
            //$$ val axis = Vector3f()
            //$$ val angle = quaternion.getAxisAngle(axis)
            //$$ Matrix4f.rotate(angle, axis, positionMatrix, positionMatrix)
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
            //$$ Matrix3f.mul(normalMatrix, createRotationMatrix(angle, axis), normalMatrix)
            //#endif
        }
    }

    override fun <R> runWithGlobalState(block: () -> R): R {
        return withGlobalStackPushed {
            applyToGlobalState()
            block()
        }
    }

    override fun <R> runReplacingGlobalState(block: () -> R): R {
        return withGlobalStackPushed {
            replaceGlobalState()
            block()
        }
    }

    private fun applyToGlobalState() {
        //#if MC >= 1.17
        //#if MC >= 1.20.5
        RenderSystem.getModelViewStack().mul(current.positionMatrix)
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.getModelViewStack().multiplyPositionMatrix(current.positionMatrix)
        //#else
        //$$ RenderSystem.getModelViewStack().method_34425(current.positionMatrix)
        //#endif
        //#if MC <= 1.21.1
        //$$ RenderSystem.applyModelViewMatrix()
        //#endif
        //#else
        //#if MC < 1.16
        //#if MC >= 1.15
        //$$ current.positionMatrix.writeToBuffer(MATRIX_BUFFER)
        //#else
        //$$ current.positionMatrix.store(MATRIX_BUFFER)
        //#endif
        //#else
        //$$ current.positionMatrix.writeRowFirst(MATRIX_BUFFER)
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

    private fun replaceGlobalState() {
        //#if MC >= 1.20.5
        RenderSystem.getModelViewStack().identity()
        //#elseif MC >= 1.17
        //$$ RenderSystem.getModelViewStack().loadIdentity()
        //#else
        //$$ GL11.glLoadIdentity()
        //#endif
        applyToGlobalState()
    }

    private inline fun <R> withGlobalStackPushed(block: () -> R) : R {
        //#if MC >= 1.17
        val stack = RenderSystem.getModelViewStack()
        //#if MC >= 1.20.5
        stack.pushMatrix()
        //#else
        //$$ stack.push()
        //#endif
        //#else
        //$$ GlStateManager.pushMatrix()
        //#endif
        return block().also {
            //#if MC >= 1.17
            //#if MC >= 1.20.5
            stack.popMatrix()
            //#else
            //$$ stack.pop()
            //#endif
            //#if MC <= 1.21.1
            //$$ RenderSystem.applyModelViewMatrix()
            //#endif
            //#else
            //$$ GlStateManager.popMatrix()
            //#endif
        }
    }
}

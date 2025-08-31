package dev.deftu.omnicore.api.client.render.stack

import dev.deftu.omnicore.internal.client.render.stack.OmniMatrixUnit
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Quaternionf

//#if MC >= 1.16.5
import net.minecraft.client.util.math.MatrixStack
//#endif

//#if MC >= 1.21.6
import org.joml.Matrix3x2f
//#endif

public interface OmniMatrixStack {
    public data class Entry(val positionMatrix: Matrix4f, val normalMatrix: Matrix3f) {
        //#if MC >= 1.16.5
        public val vanilla: MatrixStack
            get() {
                return MatrixStack().apply {
                    //#if MC >= 1.19.3
                    peek().positionMatrix.mul(positionMatrix)
                    peek().normalMatrix.mul(normalMatrix)
                    //#else
                    //$$ last().pose().multiply(positionMatrix)
                    //$$ last().normal().mul(normalMatrix)
                    //#endif
                }
            }
        //#endif

        public fun deepCopy(): Entry {
            //#if MC >= 1.19.3
            return Entry(Matrix4f(positionMatrix), Matrix3f(normalMatrix))
            //#elseif MC >= 1.15
            //$$ return Entry(positionMatrix.copy(), normalMatrix.copy())
            //#else
            //$$ return Entry(Matrix4f.load(positionMatrix, null), Matrix3f.load(normalMatrix, null))
            //#endif
        }
    }

    public val current: Entry
    public val isEmpty: Boolean

    //#if MC >= 1.16.5
    public val vanilla: MatrixStack
        get() = current.vanilla
    //#endif

    //#if MC >= 1.21.6
    public val joml: Matrix3x2f
        get() {
            return applyTo(Matrix3x2f())
        }

    public fun applyTo(dst: Matrix3x2f): Matrix3x2f {
        return dst.apply {
            val matrix = current.positionMatrix
            m00 = matrix.m00()
            m01 = matrix.m01()
            m10 = matrix.m10()
            m11 = matrix.m11()
            m20 = matrix.m30()
            m21 = matrix.m31()
        }
    }
    //#endif

    public fun deepCopy(): OmniMatrixStack

    public fun push()
    public fun pop(): Entry

    public fun translate(x: Float, y: Float, z: Float)
    public fun scale(x: Float, y: Float, z: Float)
    public fun rotate(angle: Float, axisX: Float, axisY: Float, axisZ: Float, isDegrees: Boolean = true)
    public fun rotate(quaternion: Quaternionf)

    /**
     * Apply current matrix on top of global model-view matrix.
     */
    public fun <R> runWithGlobalState(block: () -> R): R

    /**
     * Apply current matrix on top of global model-view matrix.
     */
    public fun runWithGlobalState(block: Runnable) {
        runWithGlobalState {
            block.run()
        }
    }

    /**
     * Replace global model-view matrix with current matrix for the duration of [block].
     */
    public fun <R> runReplacingGlobalState(block: () -> R): R

    /**
     * Replace global model-view matrix with current matrix for the duration of [block].
     */
    public fun runReplacingGlobalState(block: Runnable) {
        runReplacingGlobalState {
            block.run()
        }
    }

    public fun with(block: OmniMatrixStack.() -> OmniMatrixUnit) {
        push()
        try {
            block()
        } finally {
            pop()
        }
    }
}

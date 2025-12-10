package dev.deftu.omnicore.api.client.render.stack

import dev.deftu.omnicore.api.data.vec.OmniVec3d
import dev.deftu.omnicore.api.math.OmniMatrix3f
import dev.deftu.omnicore.api.math.OmniMatrix4f
import dev.deftu.omnicore.api.math.OmniQuaternion
import dev.deftu.omnicore.api.math.OmniVector2f
import dev.deftu.omnicore.api.math.OmniVector3f

//#if MC >= 1.16.5
import com.mojang.blaze3d.vertex.PoseStack
//#endif

//#if MC >= 1.21.6
import org.joml.Matrix3x2f
//#endif

public interface OmniPoseStack {
    public data class Entry(val positionMatrix: OmniMatrix4f, val normalMatrix: OmniMatrix3f) {
        //#if MC >= 1.16.5
        public val vanilla: PoseStack
            get() {
                return PoseStack().apply {
                    //#if MC >= 1.19.3
                    last().pose().mul(positionMatrix.vanilla)
                    last().normal().mul(normalMatrix.vanilla)
                    //#else
                    //$$ last().pose().multiply(positionMatrix.vanilla)
                    //$$ last().normal().mul(normalMatrix.vanilla)
                    //#endif
                }
            }
        //#endif

        public fun deepCopy(): Entry {
            return Entry(positionMatrix.deepCopy(), normalMatrix.deepCopy())
        }
    }

    public val current: Entry
    public val isEmpty: Boolean

    //#if MC >= 1.16.5
    public val vanilla: PoseStack
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
            m00 = matrix.m00
            m01 = matrix.m01
            m10 = matrix.m10
            m11 = matrix.m11
            m20 = matrix.m30
            m21 = matrix.m31
        }
    }
    //#endif

    public fun deepCopy(): OmniPoseStack

    public fun push()
    public fun push(entry: Entry)
    public fun pop(): Entry

    public fun translate(x: Float, y: Float, z: Float)
    public fun scale(x: Float, y: Float, z: Float)
    public fun rotate(angle: Float, axisX: Float, axisY: Float, axisZ: Float, isDegrees: Boolean = true)
    public fun rotate(quaternion: OmniQuaternion)

    public fun translate(vector: OmniVector3f) {
        translate(vector.x, vector.y, vector.z)
    }

    public fun translate(vec: OmniVec3d) {
        translate(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
    }

    public fun scale(vector: OmniVector3f) {
        scale(vector.x, vector.y, vector.z)
    }

    public fun scale(vec: OmniVec3d) {
        scale(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
    }

    public fun transformPosition(x: Float, y: Float, dest: OmniVector2f): OmniVector2f {
        val m = current.positionMatrix
        dest.set(
            x = m.m00 * x + m.m01 * y + m.m03,
            y = m.m10 * x + m.m11 * y + m.m13
        )
        return dest
    }

    public fun transformPosition(x: Float, y: Float): OmniVector2f {
        val dest = OmniVector2f()
        transformPosition(x, y, dest)
        return dest
    }

    public fun transformDirection(dest: OmniVector2f): OmniVector2f {
        val m = current.positionMatrix
        val x = dest.x
        val y = dest.y
        dest.set(
            x = m.m00 * x + m.m01 * y,
            y = m.m10 * x + m.m11 * y
        )
        return dest
    }

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

    public fun <T> with(block: OmniPoseStack.() -> T): T {
        push()
        return try {
            block()
        } finally {
            pop()
        }
    }

    public fun with(block: Runnable) {
        push()
        try {
            block.run()
        } finally {
            pop()
        }
    }
}

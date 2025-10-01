package dev.deftu.omnicore.internal.client.render.stack

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.math.OmniMatrix3f
import dev.deftu.omnicore.api.math.OmniMatrix4f
import dev.deftu.omnicore.api.math.OmniQuaternion
import java.util.ArrayDeque
import java.util.Deque

//#if MC >= 1.21.6
import org.joml.Matrix3x2f
//#endif

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
//$$ import com.mojang.blaze3d.platform.GlStateManager
//$$ import net.minecraft.client.util.GlAllocationUtils
//$$ import org.lwjgl.opengl.GL11
//$$ import java.nio.FloatBuffer
//$$ import java.nio.Buffer
//#endif

//#if MC < 1.16.5
//$$ import org.lwjgl.util.vector.Vector3f
//$$ import kotlin.math.cos
//$$ import kotlin.math.sin
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
        add(OmniMatrixStack.Entry(OmniMatrix4f.identity(), OmniMatrix3f.identity()))
    })

    //#if MC >= 1.16.5
    public constructor(stack: MatrixStack.Entry) : this(ArrayDeque<OmniMatrixStack.Entry>().apply {
        add(OmniMatrixStack.Entry(OmniMatrix4f.from(stack.positionMatrix), OmniMatrix3f.from(stack.normalMatrix)))
    })

    public constructor(stack: MatrixStack) : this(stack.peek())
    //#endif

    //#if MC >= 1.20.1
    public constructor(graphics: DrawContext) : this(graphics.matrices)
    //#endif

    //#if MC >= 1.21.6
    public constructor(stack: Matrix3x2f) : this() {
        with(current.positionMatrix) {
            applyFrom(toBuilder().apply {
                m00(stack.m00)
                m01(stack.m01)
                m10(stack.m10)
                m11(stack.m11)
                m30(stack.m20)
                m31(stack.m21)
            }.build())
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
            positionMatrix.translate(x, y, z)
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
            positionMatrix.scale(x, y, z)

            if (x == y && y == z) {
                if (0f > x) {
                    normalMatrix.scale(-1f)
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

                normalMatrix.scale(rt * ix, rt * iy, rt * iz)
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
            positionMatrix.rotate(axisX, axisY, axisZ, radians)
            normalMatrix.rotate(axisX, axisY, axisZ, radians)
        }
    }

    override fun rotate(quaternion: OmniQuaternion) {
        if (quaternion == OmniQuaternion.identity()) {
            return
        }

        with(current) {
            positionMatrix.rotate(quaternion)
            normalMatrix.rotate(quaternion)
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
        RenderSystem.getModelViewStack().mul(current.positionMatrix.vanilla)
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.getModelViewStack().multiplyPositionMatrix(current.positionMatrix.vanilla)
        //#else
        //$$ RenderSystem.getModelViewStack().method_34425(current.positionMatrix.vanilla)
        //#endif
        //#if MC <= 1.21.1
        //$$ RenderSystem.applyModelViewMatrix()
        //#endif
        //#else
        //#if MC < 1.16
        //#if MC >= 1.15
        //$$ current.positionMatrix.vanilla.writeToBuffer(MATRIX_BUFFER)
        //#else
        //$$ current.positionMatrix.vanilla.store(MATRIX_BUFFER)
        //#endif
        //#else
        //$$ current.positionMatrix.vanilla.writeRowFirst(MATRIX_BUFFER)
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

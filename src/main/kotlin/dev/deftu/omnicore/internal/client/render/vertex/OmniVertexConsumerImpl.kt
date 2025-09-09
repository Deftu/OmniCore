package dev.deftu.omnicore.internal.client.render.vertex

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
import org.jetbrains.annotations.ApiStatus
import java.awt.Color

//#if MC >= 1.20.6
import org.joml.Vector3f
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.render.BufferBuilder
//#else
//$$ import net.minecraft.client.renderer.BufferBuilder
//$$ import org.lwjgl.util.vector.Matrix4f
//$$ import org.lwjgl.util.vector.Matrix3f
//$$ import org.lwjgl.util.vector.Vector4f
//$$ import org.lwjgl.util.vector.Vector3f
//#endif

@ApiStatus.Internal
public open class OmniVertexConsumerImpl(
    //#if MC >= 1.16.5
    private val value: BufferBuilder,
    //#else
    //$$ private val value: BufferBuilder,
    //#endif
) : OmniVertexConsumer {

    override fun vertex(stack: OmniMatrixStack, x: Double, y: Double, z: Double): OmniVertexConsumer {
        if (stack == OmniMatrixStacks.UNIT) {
            //#if MC >= 1.21.1
            value.vertex(x.toFloat(), y.toFloat(), z.toFloat())
            //#elseif MC >= 1.16.5
            //$$ value.vertex(x, y, z)
            //#else
            //$$ value.pos(x, y, z)
            //#endif
            return this
        }

        //#if MC >= 1.16.5
        value.vertex(stack.current.positionMatrix, x.toFloat(), y.toFloat(), z.toFloat())
        //#else
        //$$ val vector = Vector4f(x.toFloat(), y.toFloat(), z.toFloat(), 1f)
        //$$ Matrix4f.transform(stack.current.positionMatrix, vector, vector)
        //$$ value.pos(vector.x.toDouble(), vector.y.toDouble(), vector.z.toDouble())
        //#endif
        return this
    }

    override fun color(red: Int, green: Int, blue: Int, alpha: Int): OmniVertexConsumer {
        value.color(red, green, blue, alpha)
        return this
    }

    override fun color(red: Float, green: Float, blue: Float, alpha: Float): OmniVertexConsumer {
        value.color(red, green, blue, alpha)
        return this
    }

    override fun color(color: Color): OmniVertexConsumer {
        return color(color.red, color.green, color.blue, color.alpha)
    }

    override fun color(color: Int): OmniVertexConsumer {
        val red = (color shr 16 and 0xFF) / 255f
        val green = (color shr 8 and 0xFF) / 255f
        val blue = (color and 0xFF) / 255f
        val alpha = (color shr 24 and 0xFF) / 255f
        return color(red, green, blue, alpha)
    }

    override fun texture(u: Double, v: Double): OmniVertexConsumer {
        //#if MC >= 1.16.5
        value.texture(u.toFloat(), v.toFloat())
        //#else
        //$$ value.tex(u, v)
        //#endif
        return this
    }

    override fun overlay(u: Int, v: Int): OmniVertexConsumer {
        //#if MC >= 1.16.5
        value.overlay(u, v)
        //#else
        //$$ value.tex(u.toDouble(), v.toDouble())
        //#endif
        return this
    }

    override fun light(u: Int, v: Int): OmniVertexConsumer {
        //#if MC >= 1.16.5
        value.light(u, v)
        //#else
        //$$ value.lightmap(u, v)
        //#endif
        return this
    }

    override fun normal(stack: OmniMatrixStack, nx: Float, ny: Float, nz: Float): OmniVertexConsumer {
        if (stack == OmniMatrixStacks.UNIT) {
            value.normal(nx, ny, nz)
            return this
        }

        //#if MC >= 1.20.6
        val normal = stack.current.normalMatrix.transform(nx, ny, nz, Vector3f())
        value.normal(normal.x, normal.y, normal.z)
        //#elseif MC >= 1.16.5
        //$$ value.normal(stack.current.normalMatrix, nx, ny, nz)
        //#else
        //$$ val vector = Vector3f(nx, ny, nz)
        //$$ Matrix3f.transform(stack.current.normalMatrix, vector, vector)
        //$$ value.normal(vector.x, vector.y, vector.z)
        //#endif
        return this
    }

    override fun next(): OmniVertexConsumer {
        //#if MC < 1.21.1
        //$$ value.endVertex()
        //#endif
        return this
    }

}

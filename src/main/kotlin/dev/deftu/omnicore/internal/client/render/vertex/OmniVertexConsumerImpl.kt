package dev.deftu.omnicore.internal.client.render.vertex

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
import org.jetbrains.annotations.ApiStatus

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
        value.vertex(stack.current.positionMatrix.vanilla, x.toFloat(), y.toFloat(), z.toFloat())
        //#else
        //$$ val vector = stack.current.positionMatrix.transformDirection(x.toFloat(), y.toFloat(), z.toFloat())
        //$$ value.pos(vector.x.toDouble(), vector.y.toDouble(), vector.z.toDouble())
        //#endif
        return this
    }

    override fun color(red: Int, green: Int, blue: Int, alpha: Int): OmniVertexConsumer {
        value.color(red, green, blue, alpha)
        return this
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

        val normal = stack.current.normalMatrix.transformDirection(nx, ny, nz)
        value.normal(normal.x, normal.y, normal.z)
        return this
    }

    override fun next(): OmniVertexConsumer {
        //#if MC < 1.21.1
        //$$ value.endVertex()
        //#endif
        return this
    }
}

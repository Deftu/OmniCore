package dev.deftu.omnicore.internal.client.render.vertex

import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStacks
import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.16.5
import com.mojang.blaze3d.vertex.BufferBuilder
//#else
//$$ import dev.deftu.omnicore.api.math.OmniVector4f
//$$ import net.minecraft.client.renderer.BufferBuilder
//#endif

@ApiStatus.Internal
public open class OmniVertexConsumerImpl(
    //#if MC >= 1.16.5
    private val value: BufferBuilder,
    //#else
    //$$ private val value: BufferBuilder,
    //#endif
) : OmniVertexConsumer {
    override fun vertex(pose: OmniPoseStack, x: Double, y: Double, z: Double): OmniVertexConsumer {
        if (pose == OmniPoseStacks.UNIT) {
            //#if MC >= 1.21.1
            value.addVertex(x.toFloat(), y.toFloat(), z.toFloat())
            //#elseif MC >= 1.16.5
            //$$ value.vertex(x, y, z)
            //#else
            //$$ value.pos(x, y, z)
            //#endif
            return this
        }

        //#if MC >= 1.16.5
        value.addVertex(pose.current.positionMatrix.vanilla, x.toFloat(), y.toFloat(), z.toFloat())
        //#else
        //$$ val (vx, vy, vz) = pose.current.positionMatrix.transform(OmniVector4f(x.toFloat(), y.toFloat(), z.toFloat(), 1f))
        //$$ value.pos(vx.toDouble(), vy.toDouble(), vz.toDouble())
        //#endif
        return this
    }

    override fun color(red: Int, green: Int, blue: Int, alpha: Int): OmniVertexConsumer {
        value.setColor(red, green, blue, alpha)
        return this
    }

    override fun texture(u: Double, v: Double): OmniVertexConsumer {
        //#if MC >= 1.16.5
        value.setUv(u.toFloat(), v.toFloat())
        //#else
        //$$ value.tex(u, v)
        //#endif
        return this
    }

    override fun overlay(u: Int, v: Int): OmniVertexConsumer {
        //#if MC >= 1.16.5
        value.setUv1(u, v)
        //#else
        //$$ value.tex(u.toDouble(), v.toDouble())
        //#endif
        return this
    }

    override fun light(u: Int, v: Int): OmniVertexConsumer {
        //#if MC >= 1.16.5
        value.setUv2(u, v)
        //#else
        //$$ value.lightmap(u, v)
        //#endif
        return this
    }

    override fun normal(pose: OmniPoseStack, nx: Float, ny: Float, nz: Float): OmniVertexConsumer {
        if (pose == OmniPoseStacks.UNIT) {
            value.setNormal(nx, ny, nz)
            return this
        }

        val normal = pose.current.normalMatrix.transformDirection(nx, ny, nz)
        value.setNormal(normal.x, normal.y, normal.z)
        return this
    }

    override fun next(): OmniVertexConsumer {
        //#if MC < 1.21.1
        //$$ value.endVertex()
        //#endif
        return this
    }
}

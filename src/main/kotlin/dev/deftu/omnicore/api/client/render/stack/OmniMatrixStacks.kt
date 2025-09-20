package dev.deftu.omnicore.api.client.render.stack

import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.internal.client.render.stack.OmniMatrixStackImpl
import dev.deftu.omnicore.internal.client.render.stack.OmniMatrixUnit

//#if MC >= 1.21.6
import org.joml.Matrix3x2f
//#endif

//#if MC >= 1.20.1
import net.minecraft.client.gui.DrawContext
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.util.math.MatrixStack
//#endif

public object OmniMatrixStacks {
    @JvmField
    public val UNIT: OmniMatrixStack = OmniMatrixUnit

    @JvmStatic
    public fun create(): OmniMatrixStack {
        return OmniMatrixStackImpl()
    }

    //#if MC >= 1.16.5
    @JvmStatic
    @VersionedAbove("1.16.5")
    public fun wrap(stack: MatrixStack): OmniMatrixStack {
        return OmniMatrixStackImpl(stack)
    }
    //#endif

    //#if MC >= 1.20.1
    @JvmStatic
    @VersionedAbove("1.20.1")
    public fun wrap(graphics: DrawContext): OmniMatrixStack {
        return OmniMatrixStackImpl(graphics)
    }
    //#endif

    //#if MC >= 1.21.6
    @JvmStatic
    @VersionedAbove("1.21.6")
    public fun wrap(matrix: Matrix3x2f): OmniMatrixStack {
        return OmniMatrixStackImpl(matrix)
    }
    //#endif

    @JvmStatic
    public fun vanilla(
        //#if MC >= 1.20.1
        graphics: DrawContext,
        //#elseif MC >= 1.16.5
        //$$ stack: MatrixStack,
        //#endif
    ): OmniMatrixStack {
        //#if MC >= 1.16.5
        return wrap(
            //#if MC >= 1.20.1
            graphics,
            //#else
            //$$ stack,
            //#endif
        )
        //#else
        //$$ return create()
        //#endif
    }
}

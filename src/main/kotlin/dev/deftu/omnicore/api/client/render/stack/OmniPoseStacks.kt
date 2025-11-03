package dev.deftu.omnicore.api.client.render.stack

import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.internal.client.render.stack.OmniPoseStackImpl
import dev.deftu.omnicore.internal.client.render.stack.OmniPoseUnit

//#if MC >= 1.21.6
import org.joml.Matrix3x2f
//#endif

//#if MC >= 1.20.1
import net.minecraft.client.gui.GuiGraphics
//#endif

//#if MC >= 1.16.5
import com.mojang.blaze3d.vertex.PoseStack
//#endif

public object OmniPoseStacks {
    @JvmField public val UNIT: OmniPoseStack = OmniPoseUnit

    @JvmStatic
    public fun create(): OmniPoseStack {
        return OmniPoseStackImpl()
    }

    //#if MC >= 1.16.5
    @JvmStatic
    @VersionedAbove("1.16.5")
    public fun wrap(pose: PoseStack): OmniPoseStack {
        return OmniPoseStackImpl(pose)
    }
    //#endif

    //#if MC >= 1.20.1
    @JvmStatic
    @VersionedAbove("1.20.1")
    public fun wrap(graphics: GuiGraphics): OmniPoseStack {
        return OmniPoseStackImpl(graphics)
    }
    //#endif

    //#if MC >= 1.21.6
    @JvmStatic
    @VersionedAbove("1.21.6")
    public fun wrap(matrix: Matrix3x2f): OmniPoseStack {
        return OmniPoseStackImpl(matrix)
    }
    //#endif

    @JvmStatic
    public fun vanilla(
        //#if MC >= 1.20.1
        graphics: GuiGraphics,
        //#elseif MC >= 1.16.5
        //$$ pose: PoseStack,
        //#endif
    ): OmniPoseStack {
        //#if MC >= 1.16.5
        return wrap(
            //#if MC >= 1.20.1
            graphics,
            //#else
            //$$ pose,
            //#endif
        )
        //#else
        //$$ return create()
        //#endif
    }

    //#if MC >= 1.20.1
    @JvmStatic
    public fun vanilla(pose: PoseStack): OmniPoseStack {
        return wrap(pose)
    }
    //#endif
}

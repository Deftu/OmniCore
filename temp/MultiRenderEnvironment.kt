package dev.deftu.omnicore

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

public object MultiRenderEnvironment {
    @JvmStatic public fun isGL21Available(): Boolean =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.openGL21
        //#endif

    @JvmStatic public fun isFramebufferEnabled(): Boolean =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.isFramebufferEnabled()
        //#endif
}

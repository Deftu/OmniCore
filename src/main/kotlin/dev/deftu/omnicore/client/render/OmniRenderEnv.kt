package dev.deftu.omnicore.client.render

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

public object OmniRenderEnv {

    @JvmStatic
    public fun isGl21Available(): Boolean =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.openGL21
        //#endif

    @JvmStatic
    public fun isFramebufferEnabled(): Boolean =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.isFramebufferEnabled()
        //#endif

}

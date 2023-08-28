package xyz.deftu.multi

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper;
//#endif

object MultiRenderEnvironment {
    @JvmStatic fun isGL21Available() =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.openGL21
        //#endif

    @JvmStatic fun isFramebufferEnabled() =
        //#if MC >= 1.15.2
        true
        //#else
        //$$ OpenGlHelper.isFramebufferEnabled()
        //#endif
}

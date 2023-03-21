package xyz.deftu.multi

//#if MC<=11202
//$$ import net.minecraft.client.renderer.OpenGlHelper;
//#endif

object MultiRenderEnvironment {
    @JvmStatic fun isGL21Available() =
        //#if MC>=11502
        true
        //#else
        //$$ OpenGlHelper.openGL21
        //#endif

    @JvmStatic fun isFramebufferEnabled() =
        //#if MC>=11502
        true
        //#else
        //$$ OpenGlHelper.isFramebufferEnabled()
        //#endif
}

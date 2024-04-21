package dev.deftu.omnicore.client.render

//#if MC <= 1.12.2
//$$ import net.minecraft.client.gui.ScaledResolution
//#endif

import dev.deftu.omnicore.client.OmniClient

public object OmniResolution {
    //#if MC <= 1.12.2
    //$$ private data class CachedScaledResolution(val width: Int, val height: Int, val scale: Int, val unicode: Boolean)
    //$$ private var scaledRes: ScaledResolution? = null
    //$$ private var cachedScaledRes: CachedScaledResolution? = null
    //#endif

    @JvmStatic
    public val screenWidth: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.width
            //#else
            //$$ return OmniClient.getInstance().displayWidth
            //#endif
        }

    @JvmStatic
    public val screenHeight: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.height
            //#else
            //$$ return OmniClient.getInstance().displayHeight
            //#endif
        }

    @JvmStatic
    public val viewportWidth: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.framebufferWidth
            //#else
            //$$ return OmniClient.getInstance().displayWidth
            //#endif
        }

    @JvmStatic
    public val viewportHeight: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.framebufferHeight
            //#else
            //$$ return OmniClient.getInstance().displayHeight
            //#endif
        }

    @JvmStatic
    public val scaledWidth: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.scaledWidth
            //#else
            //$$ return getScaledRes().scaledWidth
            //#endif
        }

    @JvmStatic
    public val scaledHeight: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.scaledHeight
            //#else
            //$$ return getScaledRes().scaledHeight
            //#endif
        }

    @JvmStatic
    public val scaleFactor: Double
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.scaleFactor
            //#else
            //$$ return getScaledRes().scaleFactor.toDouble()
            //#endif
        }

    //#if MC <= 1.12.2
    //$$ private fun getScaledRes(): ScaledResolution {
    //$$     if (scaledRes == null) {
    //$$         scaledRes = ScaledResolution(OmniClient.getInstance())
    //$$         return scaledRes!!
    //$$     }
    //$$
    //$$     val cached = CachedScaledResolution(
    //$$         OmniClient.getInstance().displayWidth,
    //$$         OmniClient.getInstance().displayHeight,
    //$$         scaledRes!!.scaleFactor,
    //$$         OmniClient.getInstance().isUnicode
    //$$     )
    //$$     if (cached != cachedScaledRes) {
    //$$         scaledRes = ScaledResolution(OmniClient.getInstance())
    //$$         cachedScaledRes = cached
    //$$     }
    //$$
    //$$     return scaledRes!!
    //$$ }
    //#endif
}

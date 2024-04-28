package dev.deftu.omnicore.client.render

//#if MC <= 1.12.2
//$$ import net.minecraft.client.gui.ScaledResolution
//#endif

import dev.deftu.omnicore.client.OmniClient

public object OmniResolution {
    //#if MC <= 1.12.2
    //$$ private data class CachedScaledResolution(val width: Int, val height: Int, val scale: Int, val isUnicode: Boolean)
    //$$ private var cachedScaledRes: CachedScaledResolution? = null
    //$$ private var scaledRes: ScaledResolution? = null
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
    //$$     val client = OmniClient.getInstance()
    //$$     val cached = CachedScaledResolution(
    //$$         client.displayWidth,
    //$$         client.displayHeight,
    //$$         client.gameSettings.guiScale,
    //$$         client.isUnicode
    //$$     )
    //$$
    //$$     if (cached != cachedScaledRes) {
    //$$         cachedScaledRes = cached
    //$$         scaledRes = ScaledResolution(client, client.displayWidth, client.displayHeight)
    //$$     }
    //$$
    //$$     return scaledRes!!
    //$$ }
    //#endif
}

package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.OmniClient

//#if MC <= 1.12.2
//$$ import net.minecraft.client.gui.ScaledResolution
//#endif

/**
 * A utility class which provides a means of checking various resolution data points.
 *
 * @since 0.1.0
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public object OmniResolution {

    //#if MC <= 1.12.2
    //$$ private data class CachedScaledResolution(val width: Int, val height: Int, val scale: Int, val isUnicode: Boolean)
    //$$
    //$$ private var cachedScaledRes: CachedScaledResolution? = null
    //$$
    //$$ private var scaledRes: ScaledResolution? = null
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use windowWidth instead", ReplaceWith("windowWidth"))
    public val screenWidth: Int
        get() = windowWidth

    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use windowHeight instead", ReplaceWith("windowHeight"))
    public val screenHeight: Int
        get() = windowHeight

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val windowWidth: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.width
            //#else
            //$$ return OmniClient.getInstance().displayWidth
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val windowHeight: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.height
            //#else
            //$$ return OmniClient.getInstance().displayHeight
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val viewportWidth: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.framebufferWidth
            //#else
            //$$ return OmniClient.getInstance().displayWidth
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val viewportHeight: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.framebufferHeight
            //#else
            //$$ return OmniClient.getInstance().displayHeight
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val scaledWidth: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.scaledWidth
            //#else
            //$$ return getScaledRes().scaledWidth
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val scaledHeight: Int
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.scaledHeight
            //#else
            //$$ return getScaledRes().scaledHeight
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val scaleFactor: Double
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().window.scaleFactor
                //#if MC >= 1.21.6
                //$$ .toDouble()
                //#endif
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
    //$$         scaledRes = ScaledResolution(client)
    //$$     }
    //$$
    //$$     return scaledRes!!
    //$$ }
    //#endif

}

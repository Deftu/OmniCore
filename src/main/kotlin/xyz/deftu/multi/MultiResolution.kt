package xyz.deftu.multi

//#if MC <= 1.12.2
//$$ import net.minecraft.client.gui.ScaledResolution
//#endif

object MultiResolution {
    //#if MC <= 1.12.2
    //$$ private data class CachedScaledResolution(val width: Int, val height: Int, val scale: Int, val unicode: Boolean)
    //$$ private var scaledRes: ScaledResolution? = null
    //$$ private var cachedScaledRes: CachedScaledResolution? = null
    //#endif

    @JvmStatic
    val screenWidth: Int
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().window.width
            //#else
            //$$ return MultiClient.getInstance().displayWidth
            //#endif
        }

    @JvmStatic
    val screenHeight: Int
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().window.height
            //#else
            //$$ return MultiClient.getInstance().displayHeight
            //#endif
        }

    @JvmStatic
    val viewportWidth: Int
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().window.framebufferWidth
            //#else
            //$$ return MultiClient.getInstance().displayWidth
            //#endif
        }

    @JvmStatic
    val viewportHeight: Int
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().window.framebufferHeight
            //#else
            //$$ return MultiClient.getInstance().displayHeight
            //#endif
        }

    @JvmStatic
    val scaledWidth: Int
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().window.scaledWidth
            //#else
            //$$ return getScaledRes().scaledWidth
            //#endif
        }

    @JvmStatic
    val scaledHeight: Int
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().window.scaledHeight
            //#else
            //$$ return getScaledRes().scaledHeight
            //#endif
        }

    @JvmStatic
    val scaleFactor: Double
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().window.scaleFactor
            //#else
            //$$ return getScaledRes().scaleFactor.toDouble()
            //#endif
        }

    //#if MC <= 1.12.2
    //$$ private fun getScaledRes(): ScaledResolution {
    //$$     if (scaledRes == null) {
    //$$         scaledRes = ScaledResolution(MultiClient.getInstance())
    //$$         return scaledRes!!
    //$$     }
    //$$
    //$$     val cached = CachedScaledResolution(
    //$$         MultiClient.getInstance().displayWidth,
    //$$         MultiClient.getInstance().displayHeight,
    //$$         scaledRes!!.scaleFactor,
    //$$         MultiClient.getInstance().isUnicode
    //$$     )
    //$$     if (cached != cachedScaledRes) {
    //$$         scaledRes = ScaledResolution(MultiClient.getInstance())
    //$$         cachedScaledRes = cached
    //$$     }
    //$$
    //$$     return scaledRes!!
    //$$ }
    //#endif
}

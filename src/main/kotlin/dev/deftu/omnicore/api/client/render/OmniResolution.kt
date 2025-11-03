package dev.deftu.omnicore.api.client.render

import net.minecraft.client.Minecraft

//#if MC <= 1.12.2
//$$ import net.minecraft.client.gui.ScaledResolution
//$$
//$$ private typealias ScreenResolution = ScaledResolution
//#endif

public object OmniResolution {
    public object Window {
        public val width: Int
            get() {
                //#if MC >= 1.16.5
                return Minecraft.getInstance().window.screenWidth
                //#else
                //$$ return Minecraft.getMinecraft().displayWidth
                //#endif
            }

        public val height: Int
            get() {
                //#if MC >= 1.16.5
                return Minecraft.getInstance().window.screenHeight
                //#else
                //$$ return Minecraft.getMinecraft().displayHeight
                //#endif
            }
    }

    public object Viewport {
        public val width: Int
            get() {
                //#if MC >= 1.16.5
                return Minecraft.getInstance().window.width
                //#else
                //$$ return Minecraft.getMinecraft().displayWidth
                //#endif
            }

        public val height: Int
            get() {
                //#if MC >= 1.16.5
                return Minecraft.getInstance().window.height
                //#else
                //$$ return Minecraft.getMinecraft().displayHeight
                //#endif
            }
    }

    public object Scaled {
        //#if MC <= 1.12.2
        //$$ private data class CachedScaledResolution(val width: Int, val height: Int, val scale: Int, val isUnicode: Boolean)
        //$$
        //$$ private var cachedScaledRes: CachedScaledResolution? = null
        //$$ private var scaledRes: ScreenResolution? = null
        //#endif

        public val width: Int
            get() {
                //#if MC >= 1.16.5
                return Minecraft.getInstance().window.guiScaledWidth
                //#else
                //$$ return getScaledRes().scaledWidth
                //#endif
            }

        public val height: Int
            get() {
                //#if MC >= 1.16.5
                return Minecraft.getInstance().window.guiScaledHeight
                //#else
                //$$ return getScaledRes().scaledHeight
                //#endif
            }

        public val scaleFactor: Double
            get() {
                //#if MC >= 1.16.5
                return Minecraft.getInstance().window.guiScale
                    //#if MC >= 1.21.6
                    .toDouble()
                //#endif
                //#else
                //$$ return getScaledRes().scaleFactor.toDouble()
                //#endif
            }

        //#if MC <= 1.12.2
        //$$ private fun getScaledRes(): ScreenResolution {
        //$$     val client = Minecraft.getMinecraft()
        //$$     val cached = CachedScaledResolution(
        //$$         client.displayWidth,
        //$$         client.displayHeight,
        //$$         client.gameSettings.guiScale,
        //$$         client.isUnicode
        //$$     )
        //$$
        //$$     if (cached != cachedScaledRes) {
        //$$         cachedScaledRes = cached
        //$$         scaledRes = ScreenResolution(client)
        //$$     }
        //$$
        //$$     return scaledRes!!
        //$$ }
        //#endif
    }

    @JvmStatic
    public val windowWidth: Int
        get() = Window.width

    @JvmStatic
    public val windowHeight: Int
        get() = Window.height

    @JvmStatic
    public val viewportWidth: Int
        get() = Viewport.width

    @JvmStatic
    public val viewportHeight: Int
        get() = Viewport.height

    @JvmStatic
    public val scaledWidth: Int
        get() = Scaled.width

    @JvmStatic
    public val scaledHeight: Int
        get() = Scaled.height

    @JvmStatic
    public val scaleFactor: Double
        get() = Scaled.scaleFactor
}

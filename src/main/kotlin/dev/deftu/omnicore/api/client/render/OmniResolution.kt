package dev.deftu.omnicore.api.client.render

import net.minecraft.client.MinecraftClient

public object OmniResolution {
    public object Window {
        public val width: Int
            get() {
                //#if MC >= 1.16.5
                return MinecraftClient.getInstance().window.width
                //#else
                //$$ return Minecraft.getMinecraft().displayWidth
                //#endif
            }

        public val height: Int
            get() {
                //#if MC >= 1.16.5
                return MinecraftClient.getInstance().window.height
                //#else
                //$$ return Minecraft.getMinecraft().displayHeight
                //#endif
            }
    }

    public object Viewport {
        //#if MC <= 1.12.2
        //$$ private data class CachedScaledResolution(val width: Int, val height: Int, val scale: Int, val isUnicode: Boolean)
        //$$
        //$$ private var cachedScaledRes: CachedScaledResolution? = null
        //$$
        //$$ private var scaledRes: ScaledResolution? = null
        //#endif

        public val width: Int
            get() {
                //#if MC >= 1.16.5
                return MinecraftClient.getInstance().window.framebufferWidth
                //#else
                //$$ return Minecraft.getMinecraft().displayWidth
                //#endif
            }

        public val height: Int
            get() {
                //#if MC >= 1.16.5
                return MinecraftClient.getInstance().window.framebufferHeight
                //#else
                //$$ return Minecraft.getMinecraft().displayHeight
                //#endif
            }
    }

    public object Scaled {
        public val width: Int
            get() {
                //#if MC >= 1.16.5
                return MinecraftClient.getInstance().window.scaledWidth
                //#else
                //$$ return getScaledRes().scaledWidth
                //#endif
            }

        public val height: Int
            get() {
                //#if MC >= 1.16.5
                return MinecraftClient.getInstance().window.scaledHeight
                //#else
                //$$ return getScaledRes().scaledHeight
                //#endif
            }

        public val scaleFactor: Double
            get() {
                //#if MC >= 1.16.5
                return MinecraftClient.getInstance().window.scaleFactor
                    //#if MC >= 1.21.6
                    .toDouble()
                    //#endif
                //#else
                //$$ return getScaledRes().scaleFactor.toDouble()
                //#endif
            }

        //#if MC <= 1.12.2
        //$$ private fun getScaledRes(): ScaledResolution {
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
        //$$         scaledRes = ScaledResolution(client)
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

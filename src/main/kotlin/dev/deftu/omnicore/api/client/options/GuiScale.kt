package dev.deftu.omnicore.api.client.options

import dev.deftu.omnicore.api.client.render.state.TrackedState

//#if MC >= 1.16.5 && MC < 1.20.5
//$$ import dev.deftu.omnicore.api.client.client
//#endif

public sealed class GuiScale : TrackedState<GuiScale> {
    public companion object {
        @JvmField public val AUTO: GuiScale = Auto
        @JvmField public val SMALL: GuiScale = Sized(1)
        @JvmField public val MEDIUM: GuiScale = Sized(2)
        @JvmField public val LARGE: GuiScale = Sized(3)
        @JvmField public val VERY_LARGE: GuiScale = Sized(4)

        /**
         * The raw integer value of the current GUI scale as provided by the Minecraft client.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        public var rawCurrentScale: Int
            get() {
                val guiScale =
                    //#if MC >= 1.19.2
                    options.guiScale.value
                    //#else
                    //$$ options.guiScale
                    //#endif

                return guiScale
            }
            set(value) {
                //#if MC >= 1.19.2
                options.guiScale.value = value
                //#else
                //$$ options.guiScale = value
                //#endif

                //#if MC >= 1.16.5 && MC < 1.20.5
                //$$ val window = client.window
                //$$ val scaleFactor = window.calculateScaleFactor(value, client.forcesUnicodeFont())
                //$$ window.scaleFactor = scaleFactor.toDouble()
                //#endif
            }

        /**
         * The current GUI scale.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        public var currentScale: GuiScale
            get() = from(rawCurrentScale)
            set(value) { rawCurrentScale = value.id }

        /**
         * Gets the GUI scale from an integer value.
         *
         * @since 0.2.1
         * @param value The integer value to get the GUI scale from.
         */
        @JvmStatic
        public fun from(value: Int): GuiScale {
            return when (value) {
                0 -> Auto
                else -> Sized(value)
            }
        }
    }

    public data object Auto : GuiScale() {
        override val id: Int
            get() = 0
    }

    public data class Sized(override val id: Int) : GuiScale()

    final override var prevState: GuiScale? = null
        private set

    public abstract val id: Int

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = currentScale
        }

        rawCurrentScale = id
    }

    override fun restore() {
        super.restore()
        prevState = null // reset since these are used as global states most of the time
    }
}

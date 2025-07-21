package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public object OmniAccessibilitySettings {

    @GameSide(Side.CLIENT)
    public object Capabilities {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isMonochromeLogoSupported: Boolean
            get() {
                //#if MC >= 1.17.1
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isHideLightningFlashesSupported: Boolean
            get() {
                //#if MC >= 1.18.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isToastDisplayTimeSupported: Boolean
            get() {
                //#if MC >= 1.19.4
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isHighContrastSupported: Boolean
            get() {
                //#if MC >= 1.19.4
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isPanoramaSpeedSupported: Boolean
            get() {
                //#if MC >= 1.19.3
                return true
                //#else
                //$$ return false
                //#endif
            }

    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isMonochromeLogoEnabled: Boolean
        get() {
            //#if MC >= 1.17.1
            return unwrap(options.monochromeLogo)
            //#else
            //$$ return false // Monochrome logo is not supported in versions before 1.17.1
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isHideLightningFlashesEnabled: Boolean
        get() {
            //#if MC >= 1.18.2
            return unwrap(options.hideLightningFlashes)
            //#else
            //$$ return false // Hide lightning flashes is not supported in versions before 1.18.2
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val toastDisplayTime: Double
        get() {
            //#if MC >= 1.19.4
            return unwrap(options.notificationDisplayTime)
            //#else
            //$$ return 0.0 // Toast display time is not supported in versions before 1.19.4
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isReduceDebugInfoEnabled: Boolean
        get() = unwrap(options.reducedDebugInfo)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isHighContrastEnabled: Boolean
        get() {
            //#if MC >= 1.19.4
            return unwrap(options.highContrast)
            //#else
            //$$ return false // High contrast is not supported in versions before 1.19.4
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val panoramaSpeed: Double
        get() {
            //#if MC >= 1.19.3
            return unwrap(options.panoramaSpeed)
            //#else
            //$$ return 0.0 // Panorama speed is not supported in versions before 1.19.3
            //#endif
        }

}

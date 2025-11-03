package dev.deftu.omnicore.api.client.options

public object OmniAccessibilitySettings {
    public object Capabilities {
        @JvmStatic
        public val isMonochromeLogoSupported: Boolean
            get() {
                //#if MC >= 1.17.1
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isHideLightningFlashesSupported: Boolean
            get() {
                //#if MC >= 1.18.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isToastDisplayTimeSupported: Boolean
            get() {
                //#if MC >= 1.19.4
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isHighContrastSupported: Boolean
            get() {
                //#if MC >= 1.19.4
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
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
    public val isMonochromeLogoEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.darkMojangStudiosBackground())
            //#elseif MC >= 1.17.1
            //$$ return unwrap(options.darkMojangStudiosBackground)
            //#else
            //$$ return false // Monochrome logo is not supported in versions before 1.17.1
            //#endif
        }

    @JvmStatic
    public val isHideLightningFlashesEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.hideLightningFlash())
            //#elseif MC >= 1.18.2
            //$$ return unwrap(options.hideLightningFlashes)
            //#else
            //$$ return false // Hide lightning flashes is not supported in versions before 1.18.2
            //#endif
        }

    @JvmStatic
    public val toastDisplayTime: Double
        get() {
            //#if MC >= 1.19.4
            return unwrap(options.notificationDisplayTime())
            //#else
            //$$ return 0.0 // Toast display time is not supported in versions before 1.19.4
            //#endif
        }

    @JvmStatic
    public val isReduceDebugInfoEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.reducedDebugInfo())
            //#else
            //$$ return unwrap(options.reducedDebugInfo)
            //#endif
        }

    @JvmStatic
    public val isHighContrastEnabled: Boolean
        get() {
            //#if MC >= 1.19.4
            return unwrap(options.highContrast())
            //#else
            //$$ return false // High contrast is not supported in versions before 1.19.4
            //#endif
        }

    @JvmStatic
    public val panoramaSpeed: Double
        get() {
            //#if MC >= 1.19.3
            return unwrap(options.panoramaSpeed())
            //#else
            //$$ return 0.0 // Panorama speed is not supported in versions before 1.19.3
            //#endif
        }
}

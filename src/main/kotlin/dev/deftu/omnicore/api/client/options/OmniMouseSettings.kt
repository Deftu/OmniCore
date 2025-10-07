package dev.deftu.omnicore.api.client.options

public object OmniMouseSettings {
    public object Capabilities {
        @JvmStatic
        public val isMouseWheelSensitivitySupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isRawMouseInputSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isDiscreteScrollSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }
    }

    @JvmStatic
    public val mouseSensitivity: Double
        get() = unwrap(options.mouseSensitivity).toDouble()

    @JvmStatic
    public val mouseWheelSensitivity: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.mouseWheelSensitivity).toDouble()
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val isRawMouseInputEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.rawMouseInput)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val isMouseYInverted: Boolean
        get() = unwrap(options.invertMouseY)

    @JvmStatic
    public val isDiscreteScrollEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.discreteMouseScroll)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val isTouchscreenMode: Boolean
        get() = unwrap(options.touchscreen)
}

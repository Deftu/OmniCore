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
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.sensitivity()).toDouble()
            //#else
            //$$ return unwrap(options.sensitivity).toDouble()
            //#endif
        }

    @JvmStatic
    public val mouseWheelSensitivity: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.mouseWheelSensitivity()).toDouble()
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.mouseWheelSensitivity).toDouble()
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    public val isRawMouseInputEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.rawMouseInput())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.rawMouseInput)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val isMouseYInverted: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.invertMouseY())
            //#else
            //$$ return unwrap(options.invertYMouse)
            //#endif
        }

    @JvmStatic
    public val isDiscreteScrollEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.discreteMouseScroll())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.discreteMouseScroll)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val isTouchscreenMode: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.touchscreen())
            //#else
            //$$ return unwrap(options.touchscreen)
            //#endif
        }
}

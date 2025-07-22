package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public object OmniMouseSettings {

    @GameSide(Side.CLIENT)
    public object Capabilities {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isMouseWheelSensitivitySupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isRawMouseInputSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
    public val mouseSensitivity: Double
        get() = unwrap(options.mouseSensitivity).toDouble()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val mouseWheelSensitivity: Double
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.mouseWheelSensitivity).toDouble()
            //#else
            //$$ return 0.0
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isRawMouseInputEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.rawMouseInput)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isMouseYInverted: Boolean
        get() = unwrap(options.invertYMouse)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isDiscreteScrollEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.discreteMouseScroll)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isTouchscreenMode: Boolean
        get() = unwrap(options.touchscreen)

}

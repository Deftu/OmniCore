package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public object OmniKeyboardSettings {

    @GameSide(Side.CLIENT)
    public object Capabilities {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isAutoJumpSupported: Boolean
            get() {
                //#if MC >= 1.12.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isToggleSneakSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isToggleSprintSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isOperatorItemsTabSupported: Boolean
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
    public val isAutoJumpEnabled: Boolean
        get() {
            //#if MC >= 1.12.2
            return unwrap(options.autoJump)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isToggleSneakEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.sneakToggled)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isToggleSprintEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return unwrap(options.sprintToggled)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isOperatorItemsTabEnabled: Boolean
        get() {
            //#if MC >= 1.19.3
            return unwrap(options.operatorItemsTab)
            //#else
            //$$ return false
            //#endif
        }

}

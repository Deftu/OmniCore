package dev.deftu.omnicore.api.client.options

public object OmniKeyboardSettings {
    public object Capabilities {

        @JvmStatic
        public val isAutoJumpSupported: Boolean
            get() {
                //#if MC >= 1.12.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isToggleSneakSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isToggleSprintSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
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
    public val isAutoJumpEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.autoJump())
            //#elseif MC >= 1.12.2
            //$$ return unwrap(options.autoJump)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val isToggleSneakEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.toggleCrouch())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.toggleCrouch)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val isToggleSprintEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.toggleSprint())
            //#elseif MC >= 1.16.5
            //$$ return unwrap(options.toggleSprint)
            //#else
            //$$ return false
            //#endif
        }

    @JvmStatic
    public val isOperatorItemsTabEnabled: Boolean
        get() {
            //#if MC >= 1.19.3
            return unwrap(options.operatorItemsTab())
            //#else
            //$$ return false
            //#endif
        }
}

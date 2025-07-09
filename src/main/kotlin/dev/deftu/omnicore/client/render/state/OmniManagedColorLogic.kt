package dev.deftu.omnicore.client.render.state

import com.mojang.blaze3d.opengl.GlStateManager

public data class OmniManagedColorLogic(
    public val isEnabled: Boolean,
    public val mode: ColorLogicMode
) {

    public fun activate() {
        if (isEnabled) {
            GlStateManager._enableColorLogicOp()
            GlStateManager._logicOp(mode.value)
        } else {
            GlStateManager._disableColorLogicOp()
        }
    }

    public companion object {

        @JvmField
        public val DISABLED: OmniManagedColorLogic = OmniManagedColorLogic(false, ColorLogicMode.CLEAR)

        @JvmStatic
        public fun active(): OmniManagedColorLogic {
            return OmniManagedColorLogic(
                isEnabled = true,
                mode = ColorLogicMode.active()
            )
        }

    }

}

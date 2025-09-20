package dev.deftu.omnicore.api.client.input

//#if MC >= 1.16.5
import dev.deftu.omnicore.api.annotations.VersionedAbove
import org.lwjgl.glfw.GLFW
//#endif

public data class KeyboardModifiers(
    public val isShift: Boolean,
    public val isCtrl: Boolean,
    public val isAlt: Boolean,
    public val isSuper: Boolean,
    public val isCapsLock: Boolean = false,
    public val isNumLock: Boolean = false
) {
    public companion object {
        @JvmStatic
        @get:JvmName("get")
        public val current: KeyboardModifiers
            get() {
                return KeyboardModifiers(
                    isShift = OmniKeyboard.isShiftKeyPressed,
                    isCtrl = OmniKeyboard.isCtrlKeyPressed,
                    isAlt = OmniKeyboard.isAltKeyPressed,
                    isSuper = OmniKeyboard.isSuperKeyPressed,
                )
            }

        //#if MC >= 1.16.5
        @JvmStatic
        @VersionedAbove("1.16.5")
        public fun wrap(mods: Int): KeyboardModifiers {
            return KeyboardModifiers(
                isShift = (mods and GLFW.GLFW_MOD_SHIFT) != 0,
                isCtrl = (mods and GLFW.GLFW_MOD_CONTROL) != 0,
                isAlt = (mods and GLFW.GLFW_MOD_ALT) != 0,
                isSuper = (mods and GLFW.GLFW_MOD_SUPER) != 0,
            )
        }
        //#endif
    }

    //#if MC >= 1.16.5
    @VersionedAbove("1.16.5")
    public fun toMods(): Int {
        return listOf(
            isShift to GLFW.GLFW_MOD_SHIFT,
            isCtrl to GLFW.GLFW_MOD_CONTROL,
            isAlt to GLFW.GLFW_MOD_ALT,
            isSuper to GLFW.GLFW_MOD_SUPER,
            isCapsLock to GLFW.GLFW_MOD_CAPS_LOCK,
            isNumLock to GLFW.GLFW_MOD_NUM_LOCK
        ).sumOf { (value, mod) ->
            if (value) mod else 0
        }
    }
    //#endif
}

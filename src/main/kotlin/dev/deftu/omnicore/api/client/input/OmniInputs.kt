package dev.deftu.omnicore.api.client.input

//#if MC >= 1.16.5
import net.minecraft.client.util.InputUtil
//#else
//$$ import net.minecraft.client.settings.GameSettings
//#endif

public object OmniInputs {
    @JvmStatic
    public fun get(code: Int): OmniInputCode {
        return when {
            OmniKeyboard.isKeyboardButton(code) -> OmniKeys.from(code)
            OmniMouse.isMouseButton(code) -> OmniMouseButtons.from(code)
            else -> throw IllegalArgumentException("Code $code is not a valid key or mouse button")
        }
    }

    @JvmStatic
    @JvmOverloads
    public fun getDisplayName(code: Int, scanCode: Int = -1): String {
        val name =
            //#if MC >= 1.21.9
            (if (code == -1) {
                InputUtil.Type.SCANCODE.createFromCode(scanCode)
            } else {
                InputUtil.Type.KEYSYM.createFromCode(code)
            }).localizedText.toString()
            //#elseif MC >= 1.16.5
            //$$ InputConstants.getKey(code, scanCode).name.toString()
            //#else
            //$$ GameSettings.getKeyDisplayString(code) ?: return "Unknown"
            //#endif
        if (name.length == 1) {
            return name.first().uppercase()
        }

        return name
    }
}

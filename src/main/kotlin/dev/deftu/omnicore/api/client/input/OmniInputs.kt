package dev.deftu.omnicore.api.client.input

//#if MC >= 1.16.5
import net.minecraft.client.util.InputUtil
//#else
//$$ import net.minecraft.client.settings.GameSettings
//#endif

public object OmniInputs {
    @JvmStatic
    @JvmOverloads
    public fun getDisplayName(code: Int, scanCode: Int = -1): String {
        val name =
            //#if MC >= 1.21.9
            //$$ (if (code == -1) {
            //$$     InputConstants.Type.SCANCODE.getOrCreate(scanCode)
            //$$ } else {
            //$$     InputConstants.Type.KEYSYM.getOrCreate(code)
            //$$ }).toString()
            //#elseif MC >= 1.16.5
            InputUtil.fromKeyCode(code, scanCode).toString()
            //#else
            //$$ GameSettings.getKeyDisplayString(code) ?: return "Unknown"
            //#endif
        if (name.length == 1) {
            return name.first().uppercase()
        }

        return name
    }
}

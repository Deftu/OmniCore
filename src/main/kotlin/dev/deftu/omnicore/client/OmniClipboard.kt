package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Incubating
import dev.deftu.omnicore.annotations.Side

@Incubating
@GameSide(Side.CLIENT)
public object OmniClipboard {

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getClipboardString(): String {
        //#if MC >= 1.16.5
        return OmniClient.getInstance().keyboard.clipboard
        //#else
        //$$ return GuiScreen.getClipboardString()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setClipboardString(string: String) {
        //#if MC >= 1.16.5
        OmniClient.getInstance().keyboard.clipboard = string
        //#else
        //$$ GuiScreen.setClipboardString(string)
        //#endif
    }

}

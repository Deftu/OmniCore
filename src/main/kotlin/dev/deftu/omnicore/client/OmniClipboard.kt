package dev.deftu.omnicore.client

import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side

//#if MC <= 1.12.2
//$$ import net.minecraft.client.gui.GuiScreen
//#endif

@GameSide(Side.CLIENT)
public object OmniClipboard {

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var clipboardString: String
        get() {
            //#if MC >= 1.16.5
            return OmniClient.getInstance().keyboard.clipboard
            //#else
            //$$ return GuiScreen.getClipboardString()
            //#endif
        }
        set(value) {
            //#if MC >= 1.16.5
            OmniClient.getInstance().keyboard.clipboard = value
            //#else
            //$$ GuiScreen.setClipboardString(value)
            //#endif
        }

}

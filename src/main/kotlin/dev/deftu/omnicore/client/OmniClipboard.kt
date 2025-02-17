package dev.deftu.omnicore.client

//#if MC <= 1.12.2
//$$ import net.minecraft.client.gui.GuiScreen
//#endif

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Incubating
import dev.deftu.omnicore.annotations.Side

@Incubating
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

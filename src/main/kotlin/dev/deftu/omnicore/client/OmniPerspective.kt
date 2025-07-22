package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.16.2
import net.minecraft.client.option.Perspective
//#endif

@GameSide(Side.CLIENT)
public object OmniPerspective {

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var perspective: Int
        get() {
            //#if MC >= 1.16.2
            return OmniClient.getInstance().options.perspective.ordinal
            //#else
            //$$ return OmniClient.getInstance().gameSettings.thirdPersonView
            //#endif
        }
        set(value) {
            //#if MC >= 1.16.2
            OmniClient.getInstance().options.perspective = Perspective.entries[perspective]
            //#else
            //$$ OmniClient.getInstance().gameSettings.thirdPersonView = perspective
            //#endif
        }

    /**
     * <= 1.16.1 will max at 2. Technically it could be expanded by other mods but tracking that might be difficult.
     * <br>
     * For modern MC perspective enum, this is completely possible to expand and track so long as this is being used.
     * @return The maximum perspective index
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public var maximumPerspective: Int = 2
        get() {
            //#if MC >= 1.16.2
            return Perspective.entries.size - 1
            //#else
            //$$ return 2
            //#endif
        }

}
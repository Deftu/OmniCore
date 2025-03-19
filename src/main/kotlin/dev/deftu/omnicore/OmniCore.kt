package dev.deftu.omnicore

import dev.deftu.eventbus.EventBus
import dev.deftu.eventbus.bus
import dev.deftu.eventbus.invokers.LMFInvoker
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.16.5
import net.minecraft.SharedConstants
//#elseif FORGE
//$$ import net.minecraftforge.common.ForgeVersion
//#endif

@GameSide(Side.BOTH)
public object OmniCore {

    @GameSide(Side.BOTH)
    public const val ID: String = "@MOD_ID@"

    /**
     * Represents OmniCore's current version.
     */
    @GameSide(Side.BOTH)
    public const val VERSION: String = "@MOD_VERSION@"

    /**
     * Represents if OmniCore is in debug mode.
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val isDebug: Boolean
        get() = System.getProperty("omnicore.debug")?.toBoolean() ?: false

    /**
     * Represents the currently running Minecraft version.
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val minecraftVersion: String
        get() =
            //#if MC >= 1.16.5
            SharedConstants.getGameVersion().name
            //#else
            //#if FORGE
            //$$ ForgeVersion.mcVersion
            //#else
            //#if MC == 1.12.2
            //$$ "1.12.2"
            //#else
            //$$ "1.8.9"
            //#endif
            //#endif
            //#endif

    @JvmStatic
    @GameSide(Side.BOTH)
    public val eventBus: EventBus = bus {
        invoker = LMFInvoker()
        threadSafety = true // Slightly decreases performance, but ensures stability
    }

}

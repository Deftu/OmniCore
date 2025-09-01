package dev.deftu.omnicore

import dev.deftu.eventbus.EventBus
import dev.deftu.eventbus.bus
import dev.deftu.eventbus.invokers.LMFInvoker
import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side

//#if MC >= 1.16.5
import net.minecraft.SharedConstants
//#elseif FORGE
//$$ import net.minecraftforge.common.ForgeVersion
//#elseif FABRIC
//$$ import net.fabricmc.loader.api.FabricLoader
//#endif

@GameSide(Side.BOTH)
public object OmniCore {

    /**
     * Represents the currently running Minecraft version.
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val minecraftVersion: String
        get() =
            //#if MC >= 1.16.5
            SharedConstants.getGameVersion().comp_4025()
            //#else
            //#if FORGE
            //$$ ForgeVersion.mcVersion
            //#else
            //$$ FabricLoader.getInstance().getModContainer("minecraft").orElseThrow {
            //$$     RuntimeException("Minecraft mod container not found")
            //$$ }.metadata.version.friendlyString
            //#endif
            //#endif

    @JvmStatic
    @GameSide(Side.BOTH)
    public val eventBus: EventBus = bus {
        invoker = LMFInvoker()
        threadSafety = true // Slightly decreases performance, but ensures stability
    }

}

package dev.deftu.omnicore


//#if MC >= 1.16.5
import net.minecraft.SharedConstants
//#else
//$$ import net.minecraftforge.common.ForgeVersion
//#endif

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

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
    @GameSide(Side.BOTH)
    public val isDebug: Boolean
        get() = System.getProperty("omnicore.debug")?.toBoolean() ?: false

    /**
     * Represents the currently running Minecraft version.
     */
    @GameSide(Side.BOTH)
    public val minecraftVersion: String
        get() =
            //#if MC >= 1.16.5
            SharedConstants.getGameVersion().name
            //#else
            //$$ ForgeVersion.mcVersion
            //#endif

}

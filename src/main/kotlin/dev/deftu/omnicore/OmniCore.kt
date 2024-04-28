package dev.deftu.omnicore

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.BOTH)
public object OmniCore {

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

}

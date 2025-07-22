package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.18.2
import net.minecraft.client.render.ChunkBuilderMode
//#endif

@GameSide(Side.CLIENT)
public enum class ChunkBuildingMode {

    NONE,
    PLAYER_AFFECTED,
    NEARBY;

    public companion object {

        //#if MC >= 1.18.2
        public fun from(vanilla: ChunkBuilderMode): ChunkBuildingMode {
            return when (vanilla) {
                ChunkBuilderMode.NONE -> NONE
                ChunkBuilderMode.PLAYER_AFFECTED -> PLAYER_AFFECTED
                ChunkBuilderMode.NEARBY -> NEARBY
            }
        }
        //#endif

    }

}

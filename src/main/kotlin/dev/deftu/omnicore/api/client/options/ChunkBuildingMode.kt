package dev.deftu.omnicore.api.client.options

//#if MC >= 1.18.2
import net.minecraft.client.render.ChunkBuilderMode
//#endif

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

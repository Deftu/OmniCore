package dev.deftu.omnicore.api.client.options

//#if MC >= 1.18.2
import net.minecraft.client.PrioritizeChunkUpdates
//#endif

public enum class ChunkBuildingMode {
    NONE,
    PLAYER_AFFECTED,
    NEARBY;

    public companion object {
        //#if MC >= 1.18.2
        public fun from(vanilla: PrioritizeChunkUpdates): ChunkBuildingMode {
            return when (vanilla) {
                PrioritizeChunkUpdates.NONE -> NONE
                PrioritizeChunkUpdates.PLAYER_AFFECTED -> PLAYER_AFFECTED
                PrioritizeChunkUpdates.NEARBY -> NEARBY
            }
        }
        //#endif
    }
}

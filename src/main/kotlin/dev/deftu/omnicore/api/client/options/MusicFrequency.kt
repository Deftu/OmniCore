package dev.deftu.omnicore.api.client.options

//#if MC >= 1.21.6
import net.minecraft.client.sounds.MusicManager
//#endif

public enum class MusicFrequency(public val index: Int) {
    DEFAULT(20),
    FREQUENT(10),
    CONSTANT(0);

    public val delayBetweenTracks: Int = index * 1_200

    public companion object {
        //#if MC >= 1.21.6
        @JvmStatic
        public fun from(vanilla: MusicManager.MusicFrequency): MusicFrequency {
            return when (vanilla) {
                MusicManager.MusicFrequency.DEFAULT -> DEFAULT
                MusicManager.MusicFrequency.FREQUENT -> FREQUENT
                MusicManager.MusicFrequency.CONSTANT -> CONSTANT
            }
        }
        //#endif
    }
}

package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.21.6
import net.minecraft.client.sound.MusicTracker
//#endif

public enum class MusicFrequency(public val index: Int) {

    DEFAULT(20),
    FREQUENT(10),
    CONSTANT(0);

    public val delayBetweenTracks: Int = index * 1_200

    public companion object {

        //#if MC >= 1.21.6
        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun from(vanilla: MusicTracker.MusicFrequency): MusicFrequency {
            return when (vanilla) {
                MusicTracker.MusicFrequency.DEFAULT -> DEFAULT
                MusicTracker.MusicFrequency.FREQUENT -> FREQUENT
                MusicTracker.MusicFrequency.CONSTANT -> CONSTANT
            }
        }
        //#endif

    }

}

package dev.deftu.omnicore.client.options

//#if MC >= 1.16.2
import net.minecraft.client.option.NarratorMode as VanillaNarratorMode
//#endif

public enum class NarratorMode {

    OFF,
    ALL,
    CHAT,
    SYSTEM;

    public companion object {

        //#if MC >= 1.16.2
        public fun from(vanilla: VanillaNarratorMode): NarratorMode {
            return when (vanilla) {
                VanillaNarratorMode.OFF -> OFF
                VanillaNarratorMode.ALL -> ALL
                VanillaNarratorMode.CHAT -> CHAT
                VanillaNarratorMode.SYSTEM -> SYSTEM
            }
        }
        //#endif

    }

}

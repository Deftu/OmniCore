package dev.deftu.omnicore.client.options

//#if MC >= 1.16
import net.minecraft.client.option.NarratorMode as VanillaNarratorMode
//#endif

public enum class NarratorMode {

    OFF,
    ALL,
    CHAT,
    SYSTEM;

    public companion object {

        //#if MC >= 1.16
        public fun from(vanilla: VanillaNarratorMode): NarratorMode {
            return when (vanilla) {
                VanillaNarratorMode.OFF -> OFF
                VanillaNarratorMode.ALL -> ALL
                VanillaNarratorMode.CHAT -> CHAT
                VanillaNarratorMode.SYSTEM -> SYSTEM
            }
        }
        //#elseif MC >= 1.12
        //$$ public fun from(vanilla: Int): NarratorMode {
        //$$     return when (vanilla) {
        //$$         0 -> OFF
        //$$         1 -> ALL
        //$$         2 -> CHAT
        //$$         3 -> SYSTEM
        //$$         else -> throw IllegalArgumentException("Unknown NarratorMode value: $vanilla")
        //$$     }
        //$$ }
        //#endif

    }

}

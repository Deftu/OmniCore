package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.16.5
import net.minecraft.client.option.CloudRenderMode as VanillaCloudRenderMode
//#endif

@GameSide(Side.CLIENT)
public enum class CloudRenderMode {

    DISABLED,
    FAST,
    FANCY;

    public companion object {

        //#if MC >= 1.16.5
        public fun from(vanilla: VanillaCloudRenderMode): CloudRenderMode {
            return when (vanilla) {
                VanillaCloudRenderMode.OFF -> DISABLED
                VanillaCloudRenderMode.FAST -> FAST
                VanillaCloudRenderMode.FANCY -> FANCY
            }
        }
        //#else
        //$$ public fun from(vanilla: Int): CloudRenderMode {
        //$$     return when (vanilla) {
        //$$         0 -> DISABLED
        //$$         1 -> FAST
        //$$         2 -> FANCY
        //$$         else -> throw IllegalArgumentException("Unknown cloud rendering mode: $vanilla")
        //$$     }
        //$$ }
        //#endif

    }

}

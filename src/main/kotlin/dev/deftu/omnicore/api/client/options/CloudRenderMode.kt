package dev.deftu.omnicore.api.client.options

//#if MC >= 1.16.5
import net.minecraft.client.CloudStatus as VanillaCloudRenderMode
//#endif

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

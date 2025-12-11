package dev.deftu.omnicore.api.client.options

//#if MC >= 1.16.5
import net.minecraft.client.GraphicsStatus
//#endif

public enum class RenderMode {
    FAST,
    FANCY,
    FABULOUS;

    public companion object {
        public val isFabulousSupported: Boolean
            get() {
                //#if MC >= 1.16.5
                return true
                //#else
                //$$ return false
                //#endif
            }

        //#if MC >= 1.16.5
        public fun from(vanilla: GraphicsStatus): RenderMode {
            return when (vanilla) {
                GraphicsStatus.FAST -> FAST
                GraphicsStatus.FANCY -> FANCY
                GraphicsStatus.FABULOUS -> FABULOUS
                //#if MC >= 1.21.11
                //$$ GraphicsPreset.CUSTOM -> FANCY // Default to FANCY for CUSTOM in newer versions
                //#endif
            }
        }
        //#endif
    }
}

package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.16.5
import net.minecraft.client.option.GraphicsMode
//#endif

@GameSide(Side.CLIENT)
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
        public fun from(vanilla: GraphicsMode): RenderMode {
            return when (vanilla) {
                GraphicsMode.FAST -> FAST
                GraphicsMode.FANCY -> FANCY
                GraphicsMode.FABULOUS -> FABULOUS
            }
        }
        //#endif

    }

}

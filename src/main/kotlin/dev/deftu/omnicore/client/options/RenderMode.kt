package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.client.option.GraphicsMode

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
                // return false
                //#endif
            }

        public fun from(vanilla: GraphicsMode): RenderMode {
            return when (vanilla) {
                GraphicsMode.FAST -> FAST
                GraphicsMode.FANCY -> FANCY
                //#if MC >= 1.16.5
                GraphicsMode.FABULOUS -> FABULOUS
                //#endif
            }
        }

    }

}

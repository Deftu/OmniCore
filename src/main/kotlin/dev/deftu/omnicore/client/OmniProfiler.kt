package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.util.profiler.Profiler

//#if MC >= 1.21.4
//$$ import net.minecraft.util.profiler.Profilers;
//#endif

/**
 * @since 0.17.0
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public object OmniProfiler {

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getInstance(): Profiler {
        //#if MC >= 1.21.4
        //$$ return Profilers.get()
        //#else
        return OmniClient.getInstance().profiler
        //#endif
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun start(name: String) {
        getInstance().push(name)
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun end() {
        getInstance().pop()
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun swap(name: String) {
        getInstance().swap(name)
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun withProfiler(name: String, runnable: Runnable) {
        try {
            // Try to pop the last section
            swap(name)
        } catch (e: Exception) {
            try {
                // If there was no last section, the above code will throw an exception, so we can ignore that and just start a new section
                start(name)
            } catch (e: Exception) {
                throw e
            }
        }

        runnable.run()
        end()
    }

}

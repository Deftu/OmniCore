package dev.deftu.omnicore.api.profiling

import net.minecraft.util.profiler.Profiler

//#if MC >= 1.21.2
import net.minecraft.util.profiler.Profilers
//#else
//$$ import dev.deftu.omnicore.client.OmniClient
//#endif

/**
 * @since 0.17.0
 * @author Deftu
 */
public object OmniProfiler {
    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    public fun getInstance(): Profiler {
        //#if MC >= 1.21.2
        return Profilers.get()
        //#else
        //$$ return OmniClient.getInstance().profiler
        //#endif
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    public fun start(name: String) {
        getInstance().push(name)
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    public fun end() {
        getInstance().pop()
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    public fun swap(name: String) {
        getInstance().swap(name)
    }

    /**
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    public fun withProfiler(name: String, runnable: Runnable) {
        try {
            try {
                swap(name)
            } catch (e: Exception) {
                // If there was no last section, the above code will throw an exception, so we can ignore that and just start a new section
                start(name)
            }

            runnable.run()
        } finally {
            end()
        }
    }

    /**
     * @since 0.40.0
     * @author Deftu
     */
    @JvmStatic
    public fun <T> withProfiler(name: String, supplier: () -> T): T {
        try {
            try {
                swap(name)
            } catch (e: Exception) {
                // If there was no last section, the above code will throw an exception, so we can ignore that and just start a new section
                start(name)
            }

            return supplier()
        } finally {
            end()
        }
    }
}

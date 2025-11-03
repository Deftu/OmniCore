package dev.deftu.omnicore.api.client.options

import dev.deftu.omnicore.api.client.client
import net.minecraft.client.Options

//#if MC >= 1.19.2
import net.minecraft.client.OptionInstance
//#endif

internal inline val options: Options
    get() = client.options

internal fun <T> unwrap(
    //#if MC >= 1.19.2
    option: OptionInstance<T>
    //#else
    //$$ option: T
    //#endif
): T {
    //#if MC >= 1.19.2
    return option.get()
    //#else
    //$$ return option
    //#endif
}

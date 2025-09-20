package dev.deftu.omnicore.api.client.options

import dev.deftu.omnicore.api.client.client
import net.minecraft.client.option.GameOptions

//#if MC >= 1.19.2
import net.minecraft.client.option.SimpleOption
//#endif

internal inline val options: GameOptions
    get() = client.options

internal fun <T> unwrap(
    //#if MC >= 1.19.2
    option: SimpleOption<T>
    //#else
    //$$ option: T
    //#endif
): T {
    //#if MC >= 1.19.2
    return option.value
    //#else
    //$$ return option
    //#endif
}

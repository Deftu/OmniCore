package dev.deftu.omnicore.api.items

import net.minecraft.item.Item

@Deprecated(message = "Unstable API. Do not use until further notice.", level = DeprecationLevel.WARNING)
public fun buildItem(block: OmniItemSettings.Builder.() -> Unit): Item {
    val settings = OmniItemSettings.Builder()
    settings.apply(block)
    return settings.build().create()
}

@Deprecated(message = "Unstable API. Do not use until further notice.", level = DeprecationLevel.WARNING)
public fun buildFood(nutrition: Int, saturation: Float, block: OmniFoodProperties.Builder.() -> Unit): OmniFoodProperties {
    val settings = OmniFoodProperties.Builder(nutrition, saturation)
    settings.apply(block)
    return settings.build()
}

package dev.deftu.omnicore.api.items

import net.minecraft.item.Item

public fun buildItem(block: OmniItemSettings.Builder.() -> Unit): Item {
    val settings = OmniItemSettings.Builder()
    settings.apply(block)
    return settings.build().create()
}

public fun buildFood(nutrition: Int, saturation: Float, block: OmniFoodProperties.Builder.() -> Unit): OmniFoodProperties {
    val settings = OmniFoodProperties.Builder(nutrition, saturation)
    settings.apply(block)
    return settings.build()
}

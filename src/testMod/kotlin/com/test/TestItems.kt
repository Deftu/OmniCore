package com.test

import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.omnicore.api.items.OmniCreativeTab
import dev.deftu.omnicore.api.items.OmniCreativeTabs
import dev.deftu.omnicore.api.items.buildFood
import dev.deftu.omnicore.api.items.buildItem
import net.minecraft.item.Item

object TestItems {
    val tab = OmniCreativeTabs.create(
        id = identifierOrThrow(ID, "example_tab"),
        icon = { exampleItem }
    )

    @JvmStatic
    val exampleItem: Item = buildItem {
        maxStackSize = 16
        maxDurability = 250
        isFireResistant = true
        tab = TestItems.tab
        food = buildFood(4, 2.4f) {
            isMeat = true
            isAlwaysEdible = true
        }
    }

    fun initialize() {
        // Force initialization
        println("Example Item: $exampleItem")
        tab.register()
    }
}

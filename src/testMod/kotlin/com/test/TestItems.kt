package com.test

import dev.deftu.omnicore.api.items.OmniCreativeTabs
import dev.deftu.omnicore.api.items.buildFood
import dev.deftu.omnicore.api.items.buildItem

object TestItems {
    @JvmStatic
    val exampleItem = buildItem {
        maxStackSize = 16
        maxDurability = 250
        isFireResistant = true
        tab = OmniCreativeTabs.buildingBlocks
        food = buildFood(4, 2.4f) {
            isMeat = true
            isAlwaysEdible = true
        }
    }

    fun initialize() {
        // Force initialization
        println("Example Item: $exampleItem")
    }
}

@file:JvmName("OmniItemStacks")

package dev.deftu.omnicore.api.items

import net.minecraft.item.ItemStack

public val ItemStack.stackAmount: Int
    get() {
        //#if MC >= 1.12.2
        return count
        //#else
        //$$ return count
        //#endif
    }

public val ItemStack.isStackEmpty: Boolean
    get() {
        //#if MC >= 1.12.2
        return isEmpty
        //#else
        //$$ return count == 0
        //#endif
    }

@file:JvmName("OmniItemStacks")

package dev.deftu.omnicore.api.items

import net.minecraft.item.ItemStack

public val ItemStack?.stackAmount: Int
    get() {
        //#if MC >= 1.12.2
        @Suppress("UsePropertyAccessSyntax") // differentiate the getter (getCount()) from the field (count)
        return this?.getCount() ?: 0
        //#else
        //$$ return this?.count ?: 0
        //#endif
    }

public val ItemStack?.isStackEmpty: Boolean
    get() {
        if (this == null) {
            return true
        }

        //#if MC >= 1.12.2
        return isEmpty
        //#else
        //$$ return count == 0
        //#endif
    }

@file:JvmName("OmniEnchantments")

package dev.deftu.omnicore.api.equipment

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack

public val ItemStack.enchantmentInfo: List<EnchantmentInfo>
    get() {
        //#if MC >= 1.20.5
        val component = EnchantmentHelper.getEnchantments(this)
        return component.enchantmentEntries.map { (enchantment, level) ->
            //#if FORGE-LIKE
            //$$ EnchantmentInfo(enchantment.value(), level)
            //#else
            EnchantmentInfo(enchantment.value(), level)
            //#endif
        }
        //#elseif MC >= 1.16.5
        //$$ return EnchantmentHelper.fromNbt(this.enchantments).map { (enchantment, level) ->
        //$$     EnchantmentInfo(enchantment, level)
        //$$ }
        //#elseif MC >= 1.12.2
        //$$ val enchantments = EnchantmentHelper.getEnchantments(this)
        //$$ return enchantments.map { (enchantment, level) ->
        //$$     EnchantmentInfo(enchantment, level)
        //$$ }
        //#else
        //$$ val enchantments = EnchantmentHelper.get(this)
        //$$ return enchantments.map { (id, level) ->
        //$$     EnchantmentInfo(Enchantment.byRawId(id), level)
        //$$ }
        //#endif
    }

public fun ItemStack.levelOf(enchantment: Enchantment): Int {
    return this.enchantmentInfo.firstOrNull { it.enchantment == enchantment }?.level ?: 0
}

public fun ItemStack.has(enchantment: Enchantment, minLevel: Int = 1): Boolean {
    return this.levelOf(enchantment) >= minLevel
}

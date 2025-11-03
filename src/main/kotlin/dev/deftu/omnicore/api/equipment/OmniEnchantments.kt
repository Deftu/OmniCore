@file:JvmName("OmniEnchantments")

package dev.deftu.omnicore.api.equipment

import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.ItemStack

public val ItemStack.enchantmentInfo: List<EnchantmentInfo>
    get() {
        //#if MC >= 1.20.5
        val component = EnchantmentHelper.getEnchantmentsForCrafting(this)
        return component.entrySet().map { (enchantment, level) ->
            //#if FORGE-LIKE
            EnchantmentInfo(enchantment.value(), level)
            //#else
            //$$ EnchantmentInfo(enchantment.value(), level)
            //#endif
        }
        //#elseif MC >= 1.16.5
        //$$ return EnchantmentHelper.deserializeEnchantments(this.enchantmentTags).map { (enchantment, level) ->
        //$$     EnchantmentInfo(enchantment, level)
        //$$ }
        //#elseif MC >= 1.12.2
        //$$ val enchantments = EnchantmentHelper.getEnchantments(this)
        //$$ return enchantments.map { (enchantment, level) ->
        //$$     EnchantmentInfo(enchantment, level)
        //$$ }
        //#else
        //$$ val enchantments = EnchantmentHelper.getEnchantments(this)
        //$$ return enchantments.map { (id, level) ->
        //$$     EnchantmentInfo(Enchantment.getEnchantmentById(id), level)
        //$$ }
        //#endif
    }

public fun ItemStack.levelOf(enchantment: Enchantment): Int {
    return this.enchantmentInfo.firstOrNull { it.enchantment == enchantment }?.level ?: 0
}

public fun ItemStack.has(enchantment: Enchantment, minLevel: Int = 1): Boolean {
    return this.levelOf(enchantment) >= minLevel
}

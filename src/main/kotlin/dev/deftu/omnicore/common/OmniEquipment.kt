package dev.deftu.omnicore.common

//#if MC >= 1.12.2
import net.minecraft.entity.EquipmentSlot
//#endif

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Incubating
import dev.deftu.omnicore.annotations.Side
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

@Incubating
@GameSide(Side.BOTH)
public object OmniEquipment {

    @GameSide(Side.BOTH)
    public enum class EquipmentType {
        MAIN_HAND,
        OFF_HAND,
        HEAD,
        CHEST,
        LEGS,
        FEET;

        @GameSide(Side.BOTH)
        public val isHand: Boolean
            get() = this == MAIN_HAND || this == OFF_HAND
    }

    @GameSide(Side.BOTH)
    public data class EnchantmentInfo(
        public val enchantment: Enchantment,
        public val level: Int
    )

    @GameSide(Side.BOTH)
    public fun getEquipment(
        entity: LivingEntity,
        type: EquipmentType
    ): ItemStack {
        if (type.isHand) {
            //#if MC >= 1.12.2
            return if (type == EquipmentType.MAIN_HAND) entity.mainHandStack else entity.offHandStack
            //#else
            //$$ return entity.heldItem
            //#endif
        }

        val vanillaValue =
            when (type) {
                //#if MC >= 1.12.2
                EquipmentType.HEAD -> EquipmentSlot.HEAD
                EquipmentType.CHEST -> EquipmentSlot.CHEST
                EquipmentType.LEGS -> EquipmentSlot.LEGS
                EquipmentType.FEET -> EquipmentSlot.FEET
                //#else
                //$$ EquipmentType.HEAD -> 3
                //$$ EquipmentType.CHEST -> 2
                //$$ EquipmentType.LEGS -> 1
                //$$ EquipmentType.FEET -> 0
                //#endif
                else -> throw IllegalArgumentException("Invalid equipment type $type") // Should never happen
            }
        //#if MC >= 1.12.2
        return entity.getEquippedStack(vanillaValue)
        //#else
        //$$ return entity.getCurrentArmor(vanillaValue)
        //#endif
    }

    @GameSide(Side.BOTH)
    public fun getStackEnchantments(stack: ItemStack): List<EnchantmentInfo> {
        //#if MC >= 1.16.5
        return EnchantmentHelper.fromNbt(stack.enchantments).map { (enchantment, level) ->
            EnchantmentInfo(enchantment, level)
        }
        //#elseif MC >= 1.12.2
        //$$ return EnchantmentHelper.getEnchantments(stack).map { (enchantment, level) ->
        //$$     EnchantmentInfo(enchantment, level)
        //$$ }
        //#else
        //$$ val enchantments = EnchantmentHelper.getEnchantments(stack)
        //$$ return enchantments.map { (id, level) ->
        //$$     EnchantmentInfo(Enchantment.getEnchantmentById(id), level)
        //$$ }
        //#endif
    }

    @GameSide(Side.BOTH)
    public fun getStackAmount(stack: ItemStack): Int {
        //#if MC >= 1.12.2
        return stack.count
        //#else
        //$$ return stack.stackSize
        //#endif
    }

    @GameSide(Side.BOTH)
    public fun isStackEmpty(stack: ItemStack): Boolean {
        //#if MC >= 1.12.2
        return stack.isEmpty
        //#else
        //$$ return stack.stackSize == 0
        //#endif
    }

}

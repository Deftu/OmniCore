@file:JvmName("OmniEquipment")

package dev.deftu.omnicore.api.equipment

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

public val EquipmentType.isHand: Boolean
    get() = this is EquipmentType.MainHand || this is EquipmentType.OffHand

public val EquipmentType.isArmor: Boolean
    get() = this is EquipmentType.Armor

public fun LivingEntity.stack(target: EquipmentType): ItemStack {
    return when (target) {
        is EquipmentType.MainHand -> this.mainHandStack
        is EquipmentType.OffHand -> this.offHandStack
        is EquipmentType.Armor -> this.getEquippedStack(target.slot)
    }
}

public operator fun LivingEntity.get(target: EquipmentType): ItemStack {
    return this.stack(target)
}
